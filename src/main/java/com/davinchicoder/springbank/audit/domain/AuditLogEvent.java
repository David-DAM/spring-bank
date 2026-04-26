package com.davinchicoder.springbank.audit.domain;

import com.davinchicoder.springbank.common.domain.DomainEvent;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AuditLogEvent(
        String eventId,
        List<DomainEvent> domainEvent,
        Instant occurredAt
) implements DomainEvent {

    public static AuditLogEvent of(List<DomainEvent> domainEvents) {
        return new AuditLogEvent(
                UUID.randomUUID().toString(),
                domainEvents,
                Instant.now()
        );
    }

}
