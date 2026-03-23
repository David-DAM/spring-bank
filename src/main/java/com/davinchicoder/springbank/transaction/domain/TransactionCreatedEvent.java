package com.davinchicoder.springbank.transaction.domain;

import com.davinchicoder.springbank.common.domain.DomainEvent;

import java.time.Instant;

public record TransactionCreatedEvent(
        String eventId,
        String transactionId,
        String customerId,
        Instant occurredAt
) implements DomainEvent {

    public static TransactionCreatedEvent of(Transaction transaction) {
        return new TransactionCreatedEvent(
                transaction.getId(),
                transaction.getId(),
                transaction.getAccountNumber(),
                transaction.getTimestamp()
        );
    }

}
