package com.davinchicoder.springbank.transaction.domain;

import com.davinchicoder.springbank.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record TransactionCreatedEvent(
        String eventId,
        String transactionId,
        Instant occurredAt
) implements DomainEvent {

    public static TransactionCreatedEvent of(Transaction transaction) {
        return new TransactionCreatedEvent(
                UUID.randomUUID().toString(),
                transaction.getId(),
                transaction.getTimestamp()
        );
    }

}
