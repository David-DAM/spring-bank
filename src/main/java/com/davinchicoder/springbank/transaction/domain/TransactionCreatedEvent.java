package com.davinchicoder.springbank.transaction.domain;

import com.davinchicoder.springbank.common.domain.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionCreatedEvent(
        String eventId,
        String transactionId,
        BigDecimal amount,
        String fromAccount,
        String toAccount,
        Instant occurredAt
) implements DomainEvent {

    public static TransactionCreatedEvent of(String transactionId, BigDecimal amount, String fromAccount, String toAccount) {
        return new TransactionCreatedEvent(
                UUID.randomUUID().toString(),
                transactionId,
                amount,
                fromAccount,
                toAccount,
                Instant.now()
        );
    }

}
