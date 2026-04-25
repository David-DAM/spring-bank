package com.davinchicoder.springbank.outbox.infrastructure.database;

public enum OutboxStatus {
    PENDING,
    PROCESSING,
    PROCESSED,
    FAILED
}
