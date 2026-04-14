package com.davinchicoder.springbank.transaction.application.request;

import com.davinchicoder.springbank.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record NewTransactionRequest(
        String id,
        String fromAccount,
        String toAccount,
        BigDecimal amount,
        TransactionType type,
        Instant createdAt
) {
}
