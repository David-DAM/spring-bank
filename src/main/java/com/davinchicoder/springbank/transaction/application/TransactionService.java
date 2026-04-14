package com.davinchicoder.springbank.transaction.application;

import com.davinchicoder.springbank.ledger.domain.EntryType;
import com.davinchicoder.springbank.ledger.domain.LedgerEntry;
import com.davinchicoder.springbank.ledger.infrastructure.LedgerEntryRepository;
import com.davinchicoder.springbank.outbox.infrastructure.database.OutboxEventRepository;
import com.davinchicoder.springbank.transaction.application.request.NewTransactionRequest;
import com.davinchicoder.springbank.transaction.application.request.NewTransactionResponse;
import com.davinchicoder.springbank.transaction.domain.Transaction;
import com.davinchicoder.springbank.transaction.domain.TransactionCreatedEvent;
import com.davinchicoder.springbank.transaction.infrastructure.database.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final OutboxEventRepository eventRepository;

    @Retryable(maxRetries = 3)
    @Transactional(rollbackFor = Exception.class)
    public NewTransactionResponse createTransaction(NewTransactionRequest request) {

        Optional<Transaction> optionalTransaction = transactionRepository.findByIdempotencyKey(request.id());

        if (optionalTransaction.isPresent()) {
            log.info("Transaction already exists: {}", request.id());
            return null;
        }

        Transaction transaction = Transaction.builder()
                .idempotencyKey(request.id())
                .timestamp(request.createdAt())
                .build();

        Transaction saved = transactionRepository.insert(transaction);

        LedgerEntry debit = LedgerEntry.builder()
                .transactionId(saved.getId())
                .accountId(request.fromAccount())
                .type(EntryType.DEBIT)
                .amount(request.amount())
                .build();

        LedgerEntry credit = LedgerEntry.builder()
                .transactionId(saved.getId())
                .accountId(request.toAccount())
                .type(EntryType.CREDIT)
                .amount(request.amount())
                .build();

        validateBalanced(List.of(debit, credit));

        ledgerEntryRepository.upsertAll(List.of(debit, credit));

        eventRepository.insertAll(List.of(TransactionCreatedEvent.of(saved)));

        return NewTransactionResponse.of(saved);
    }

    private void validateBalanced(List<LedgerEntry> entries) {
        BigDecimal debit = entries.stream()
                .filter(e -> EntryType.DEBIT.equals(e.getType()))
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal credit = entries.stream()
                .filter(e -> EntryType.CREDIT.equals(e.getType()))
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!debit.equals(credit)) {
            throw new IllegalStateException("Unbalanced transaction");
        }
    }

}
