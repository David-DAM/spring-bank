package com.davinchicoder.springbank.audit.domain;

import com.davinchicoder.springbank.common.domain.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record AuditLogEvent(
        String eventId,
        String eventType,
        String domainEvent,
        Instant occurredAt
) implements DomainEvent {

    public static AuditLogEvent of(DomainEvent domainEvent) {
        return new AuditLogEvent(
                UUID.randomUUID().toString(),
                domainEvent.getClass().getSimpleName(),
                domainEvent.toString(),
                Instant.now()
        );
    }

}
