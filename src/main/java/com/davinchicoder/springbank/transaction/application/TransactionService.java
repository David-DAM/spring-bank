package com.davinchicoder.springbank.transaction.application;

import com.davinchicoder.springbank.account.domain.AccountNotFoundException;
import com.davinchicoder.springbank.audit.domain.AuditLogEvent;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
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

        publishDomainEvents(saved, request);
        
        return NewTransactionResponse.of(saved);

    }

    private void publishDomainEvents(Transaction saved, NewTransactionRequest request) {

        TransactionCreatedEvent transactionCreatedEvent = TransactionCreatedEvent.of(saved.getId(), request.amount(), request.fromAccount(), request.toAccount());

        AuditLogEvent auditLogEvent = AuditLogEvent.of(transactionCreatedEvent);

        eventRepository.insertAll(List.of(transactionCreatedEvent, auditLogEvent));
    }


    private Transaction saveTransaction(NewTransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .idempotencyKey(request.idempotencyKey())
                .type(request.type())
                .build();

        return transactionRepository.insert(transaction);
    }


}
