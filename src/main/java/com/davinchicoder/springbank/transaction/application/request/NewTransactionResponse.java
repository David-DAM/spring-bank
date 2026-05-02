package com.davinchicoder.springbank.transaction.application.request;

import com.davinchicoder.springbank.transaction.domain.Transaction;
import com.davinchicoder.springbank.transaction.domain.TransactionStatus;

import java.time.Instant;

public record NewTransactionResponse(
        String id,
        TransactionStatus status,
        Instant createdAt
) {

    public static NewTransactionResponse of(Transaction transaction) {
        return new NewTransactionResponse(transaction.getId(), transaction.getStatus(), Instant.now());
    }

}
