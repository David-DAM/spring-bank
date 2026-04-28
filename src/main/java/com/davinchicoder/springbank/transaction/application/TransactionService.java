package com.davinchicoder.springbank.transaction.application;

import com.davinchicoder.springbank.account.domain.Account;
import com.davinchicoder.springbank.account.domain.AccountNotFoundException;
import com.davinchicoder.springbank.account.infrastructure.AccountRepository;
import com.davinchicoder.springbank.audit.domain.AuditLogEvent;
import com.davinchicoder.springbank.ledger.domain.EntryType;
import com.davinchicoder.springbank.ledger.domain.LedgerEntry;
import com.davinchicoder.springbank.ledger.infrastructure.database.LedgerEntryRepository;
import com.davinchicoder.springbank.outbox.infrastructure.database.OutboxEventRepository;
import com.davinchicoder.springbank.transaction.application.request.NewTransactionRequest;
import com.davinchicoder.springbank.transaction.application.request.NewTransactionResponse;
import com.davinchicoder.springbank.transaction.domain.InvalidTransactionException;
import com.davinchicoder.springbank.transaction.domain.Transaction;
import com.davinchicoder.springbank.transaction.domain.TransactionCreatedEvent;
import com.davinchicoder.springbank.transaction.infrastructure.database.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;
    private final OutboxEventRepository eventRepository;

    @Retryable(maxRetries = 3)
    @Transactional(noRollbackFor = {InvalidTransactionException.class, AccountNotFoundException.class})
    public NewTransactionResponse createTransaction(NewTransactionRequest request) {
        log.info("Received transaction request: {}", request);
        Optional<Transaction> optionalTransaction = transactionRepository.findByIdempotencyKey(request.idempotencyKey());

        if (optionalTransaction.isPresent()) {
            log.info("Transaction already exists: {}", request.id());
            return NewTransactionResponse.of(optionalTransaction.get());
        }

        Transaction saved = saveTransaction(request);

        try {
            validateTransaction(request);

            validateBalance(request);

            Transaction reserved = reserveTransaction(saved);

            createLedgerEntries(request, reserved);

            Transaction completed = completeTransaction(reserved);

            publishDomainEvents(completed);

            return NewTransactionResponse.of(completed);

        } catch (InvalidTransactionException | AccountNotFoundException e) {
            log.error("Invalid transaction: {}", request.id(), e);
            saved.fail();
            transactionRepository.update(saved);
            throw e;
        } catch (Exception e) {
            log.error("Error processing transaction: {}", request.id(), e);
            throw e;
        }
    }

    private Transaction completeTransaction(Transaction reserved) {
        reserved.complete();
        return transactionRepository.update(reserved);
    }

    private Transaction reserveTransaction(Transaction saved) {
        saved.reserve();
        return transactionRepository.update(saved);
    }

    private void validateBalance(NewTransactionRequest request) {
        Long balanceInCents = ledgerEntryRepository.calculateBalanceInCents(request.fromAccount());
        if (balanceInCents.compareTo(request.amount().movePointRight(2).longValueExact()) < 0) {
            log.error("Insufficient funds: {}", request.fromAccount());
            throw new InvalidTransactionException("Insufficient funds");
        }
    }

    private void validateTransaction(NewTransactionRequest request) {
        if (request.fromAccount().equals(request.toAccount())) {
            log.error("Self-transfer not allowed: {}", request.fromAccount());
            throw new InvalidTransactionException("Self-transfer not allowed");
        }

        if (request.amount().compareTo(BigDecimal.valueOf(100000)) > 0) {
            log.error("Transaction amount too high: {}", request.amount());
            throw new InvalidTransactionException("Transaction amount too high");
        }

        Optional<Account> optionalFrom = accountRepository.findByIban(request.fromAccount());

        if (optionalFrom.isEmpty()) {
            log.error("Account not found: {}", request.fromAccount());
            throw new AccountNotFoundException("Account not found %s".formatted(request.fromAccount()));
        }

        optionalFrom.get().validateCanOperate();

        Optional<Account> optionalTo = accountRepository.findByIban(request.toAccount());

        if (optionalTo.isEmpty()) {
            log.error("Account not found: {}", request.toAccount());
            throw new AccountNotFoundException("Account not found %s".formatted(request.toAccount()));
        }

        optionalTo.get().validateCanOperate();
    }

    private void publishDomainEvents(Transaction saved) {

        TransactionCreatedEvent transactionCreatedEvent = TransactionCreatedEvent.of(saved);

        AuditLogEvent auditLogEvent = AuditLogEvent.of(List.of(transactionCreatedEvent));

        eventRepository.insertAll(List.of(transactionCreatedEvent, auditLogEvent));
    }

    private void createLedgerEntries(NewTransactionRequest request, Transaction saved) {

        long amountInCents = request.amount().movePointRight(2).longValueExact();

        LedgerEntry debit = LedgerEntry.builder()
                .transactionId(saved.getId())
                .accountId(request.fromAccount())
                .type(EntryType.DEBIT)
                .amount(amountInCents)
                .createdAt(Instant.now())
                .build();

        LedgerEntry credit = LedgerEntry.builder()
                .transactionId(saved.getId())
                .accountId(request.toAccount())
                .type(EntryType.CREDIT)
                .amount(amountInCents)
                .createdAt(Instant.now())
                .build();

        ledgerEntryRepository.upsertAll(List.of(debit, credit));
    }

    private Transaction saveTransaction(NewTransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .idempotencyKey(request.idempotencyKey())
                .timestamp(request.createdAt())
                .build();

        return transactionRepository.insert(transaction);
    }


}
