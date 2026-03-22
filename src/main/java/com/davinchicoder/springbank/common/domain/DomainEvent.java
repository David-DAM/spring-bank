package com.davinchicoder.springbank.common.domain;

import java.time.Instant;

public interface DomainEvent {

    String eventId();

    Instant occurredAt();
}
