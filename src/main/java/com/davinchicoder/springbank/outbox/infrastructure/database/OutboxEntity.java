package com.davinchicoder.springbank.outbox.infrastructure.database;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "outbox_events")
public class OutboxEntity {
    
    @Id
    private String id;

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Instant occurredAt;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private int retryCount;

    private Instant lastAttemptAt;

    private Instant processedAt;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

}
