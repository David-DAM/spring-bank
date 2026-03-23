package com.davinchicoder.springbank.transaction.application;

import com.davinchicoder.springbank.transaction.domain.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;

public record NewTransactionRequest(
        String id,
        String accountNumber,
        BigDecimal amount,
        TransactionType type,
        Instant timestamp
) {
}
