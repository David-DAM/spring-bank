package com.davinchicoder.springbank.transaction.application;

import com.davinchicoder.springbank.outbox.infrastructure.database.OutboxEventRepository;
import com.davinchicoder.springbank.transaction.domain.Transaction;
import com.davinchicoder.springbank.transaction.domain.TransactionCreatedEvent;
import com.davinchicoder.springbank.transaction.infrastructure.database.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final OutboxEventRepository eventRepository;

    @Retryable(maxRetries = 3)
    @Transactional
    public void createTransaction(NewTransactionRequest request) {

        Optional<Transaction> optionalTransaction = repository.findByIdempotencyKey(request.id());

        if (optionalTransaction.isPresent()) {
            log.info("Transaction already exists: {}", request.id());
            return;
        }

        Transaction transaction = Transaction.builder()
                .idempotencyKey(request.id())
                .amount(request.amount())
                .accountNumber(request.accountNumber())
                .type(request.type())
                .timestamp(request.timestamp())
                .build();

        Transaction saved = repository.insert(transaction);

        eventRepository.insertAll(List.of(TransactionCreatedEvent.of(saved)));

    }

}
