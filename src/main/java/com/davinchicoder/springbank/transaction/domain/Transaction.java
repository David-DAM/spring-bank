package com.davinchicoder.springbank.transaction.domain;

import com.davinchicoder.springbank.audit.domain.AuditableDomain;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class Transaction extends AuditableDomain {

    private String id;
    private String idempotencyKey;
    private Long version;
    private TransactionType type;
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;

    public void reserve() {
        ensureState(TransactionStatus.PENDING);
        this.status = TransactionStatus.RESERVED;
    }

    public void complete() {
        ensureState(TransactionStatus.RESERVED);
        this.status = TransactionStatus.COMPLETED;
    }

    public void fail() {
        this.status = TransactionStatus.FAILED;
    }

    private void ensureState(TransactionStatus expected) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "Invalid transition from %s expected %s".formatted(status, expected)
            );
        }
    }

}
