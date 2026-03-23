package com.davinchicoder.springbank.transaction.infrastructure.database;

import com.davinchicoder.springbank.transaction.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final TransactionQueryRepository repository;
    private final TransactionEntityMapper mapper;

    public Optional<Transaction> findById(String id) {
        return repository.findById(id).map(mapper::toTransaction);
    }

    public Transaction upsert(Transaction transaction) {
        TransactionEntity entity = mapper.toTransactionEntity(transaction);

        TransactionEntity saved = repository.save(entity);

        return mapper.toTransaction(saved);
    }
}
