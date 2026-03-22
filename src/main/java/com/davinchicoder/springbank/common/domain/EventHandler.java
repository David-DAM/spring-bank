package com.davinchicoder.springbank.common.domain;

public interface EventHandler<T extends DomainEvent> {

    String eventType();

    Class<T> payloadType();

    void handle(T payload);
}
