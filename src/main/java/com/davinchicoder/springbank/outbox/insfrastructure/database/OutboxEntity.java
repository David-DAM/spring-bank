package com.davinchicoder.springbank.outbox.insfrastructure.database;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class OutboxEntity {

    private String id;
    private String eventType;
    private String payload;
    private Instant occurredAt;
    private boolean processed;

}
