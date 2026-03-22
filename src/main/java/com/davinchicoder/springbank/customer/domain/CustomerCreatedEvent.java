package com.davinchicoder.springbank.customer.domain;

import com.davinchicoder.springbank.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CustomerCreatedEvent(
        String eventId,
        String customerId,
        String name,
        String email,
        Instant occurredAt
) implements DomainEvent {

    public static CustomerCreatedEvent of(Customer customer) {
        return new CustomerCreatedEvent(
                UUID.randomUUID().toString(),
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                Instant.now()
        );
    }
}
