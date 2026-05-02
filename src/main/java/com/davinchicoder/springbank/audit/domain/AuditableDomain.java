package com.davinchicoder.springbank.audit.domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableDomain {

    protected Instant createdAt;

    protected Instant updatedAt;

    private String createdBy;

    private String updatedBy;
}
