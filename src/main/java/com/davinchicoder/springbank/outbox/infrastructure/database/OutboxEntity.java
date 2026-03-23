package com.davinchicoder.springbank.outbox.infrastructure.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private boolean processed;

}
