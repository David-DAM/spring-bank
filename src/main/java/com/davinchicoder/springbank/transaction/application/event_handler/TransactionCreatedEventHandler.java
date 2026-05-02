package com.davinchicoder.springbank.transaction.application.event_handler;

import com.davinchicoder.springbank.account.domain.Account;
import com.davinchicoder.springbank.account.domain.AccountNotFoundException;
import com.davinchicoder.springbank.account.infrastructure.AccountRepository;
import com.davinchicoder.springbank.common.domain.EventHandler;
import com.davinchicoder.springbank.ledger.domain.EntryType;
import com.davinchicoder.springbank.ledger.domain.LedgerEntry;
import com.davinchicoder.springbank.ledger.infrastructure.database.LedgerEntryRepository;
import com.davinchicoder.springbank.transaction.domain.InvalidTransactionException;
import com.davinchicoder.springbank.transaction.domain.Transaction;
import com.davinchicoder.springbank.transaction.domain.TransactionCreatedEvent;
import com.davinchicoder.springbank.transaction.domain.TransactionNotFoundException;
import com.davinchicoder.springbank.transaction.infrastructure.database.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionCreatedEventHandler implements EventHandler<TransactionCreatedEvent> {

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountRepository accountRepository;

    @Override
    public String eventType() {
        return TransactionCreatedEvent.class.getSimpleName();
    }

    @Override
    public Class<TransactionCreatedEvent> payloadType() {
        return TransactionCreatedEvent.class;
    }

    @Transactional
    @Override
    public void handle(TransactionCreatedEvent event) {
        log.info("Received TransactionCreatedEvent: {}", event.transactionId());

        Transaction transaction = transactionRepository.findById(event.transactionId()).orElseThrow(() -> new TransactionNotFoundException(event.transactionId()));

        try {
            validateTransaction(event);

            Account fromAccount = getFromAccount(event);

            Long balanceInCents = calculateBalanceInCents(event);

            updateAccount(fromAccount, balanceInCents);

            Transaction reserved = reserveTransaction(transaction);

            createLedgerEntries(event, reserved);

            completeTransaction(reserved);


        } catch (InvalidTransactionException | AccountNotFoundException e) {
            log.error("Invalid transaction: {}", transaction.getId(), e);
            transaction.fail();
            transactionRepository.update(transaction);

        } catch (Exception e) {
            log.error("Error processing transaction: {}", transaction.getId(), e);
            throw e;
        }
    }

    private void updateAccount(Account fromAccount, Long balanceInCents) {
        fromAccount.setBalanceInCents(balanceInCents);

        accountRepository.upsert(fromAccount);
    }

    private void completeTransaction(Transaction reserved) {
        reserved.complete();
        transactionRepository.update(reserved);
    }

    private Transaction reserveTransaction(Transaction saved) {
        saved.reserve();
        return transactionRepository.update(saved);
    }

    private Long calculateBalanceInCents(TransactionCreatedEvent request) {
        Long balanceInCents = ledgerEntryRepository.calculateBalanceInCents(request.fromAccount());
        long transactionAmountInCents = request.amount().movePointRight(2).longValueExact();
        if (balanceInCents.compareTo(transactionAmountInCents) < 0) {
            log.error("Insufficient funds: {}", request.fromAccount());
            throw new InvalidTransactionException("Insufficient funds");
        }
        return balanceInCents - transactionAmountInCents;
    }

    private Account getFromAccount(TransactionCreatedEvent request) {

        Account fromAccount = accountRepository.findByIban(request.fromAccount()).orElseThrow(() -> new AccountNotFoundException("Account not found %s".formatted(request.fromAccount())));

        fromAccount.validateCanOperate();

        Account toAccount = accountRepository.findByIban(request.toAccount()).orElseThrow(() -> new AccountNotFoundException("Account not found %s".formatted(request.toAccount())));

        toAccount.validateCanOperate();

        return fromAccount;
    }

    private void validateTransaction(TransactionCreatedEvent request) {
        if (request.fromAccount().equals(request.toAccount())) {
            log.error("Self-transfer not allowed: {}", request.fromAccount());
            throw new InvalidTransactionException("Self-transfer not allowed");
        }

        if (request.amount().compareTo(BigDecimal.valueOf(100000)) > 0) {
            log.error("Transaction amount too high: {}", request.amount());
            throw new InvalidTransactionException("Transaction amount too high");
        }
    }


    private void createLedgerEntries(TransactionCreatedEvent event, Transaction saved) {

        long amountInCents = event.amount().movePointRight(2).longValueExact();

        LedgerEntry debit = LedgerEntry.builder()
                .transactionId(saved.getId())
                .accountId(event.fromAccount())
                .type(EntryType.DEBIT)
                .amount(amountInCents)
                .build();

        LedgerEntry credit = LedgerEntry.builder()
                .transactionId(saved.getId())
                .accountId(event.toAccount())
                .type(EntryType.CREDIT)
                .amount(amountInCents)
                .build();

        ledgerEntryRepository.upsertAll(List.of(debit, credit));
    }
}
