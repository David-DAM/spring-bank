package com.davinchicoder.springbank.transaction.infrastructure.database;

import com.davinchicoder.springbank.transaction.domain.Transaction;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final TransactionQueryRepository repository;
    private final TransactionEntityMapper mapper;
    private final EntityManager entityManager;

    public Optional<Transaction> findById(String id) {
        return repository.findById(id).map(mapper::toTransaction);
    }

    public Optional<Transaction> findByIdempotencyKey(String idempotencyKey) {
        return repository.findByIdempotencyKey(idempotencyKey).map(mapper::toTransaction);
    }

    public Transaction insert(Transaction transaction) {
        TransactionEntity entity = mapper.toTransactionEntity(transaction);

        entityManager.persist(entity);

        return mapper.toTransaction(entity);
    }

    public Transaction update(Transaction transaction) {
        TransactionEntity entity = mapper.toTransactionEntity(transaction);

        TransactionEntity saved = repository.save(entity);

        return mapper.toTransaction(saved);
    }
}
