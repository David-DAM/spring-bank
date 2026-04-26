package com.davinchicoder.springbank.audit.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "audits")
public class AuditEntity {

    @Id
    private String id;

    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String userId;

    private String correlationId;

    private Instant createdAt;

}
