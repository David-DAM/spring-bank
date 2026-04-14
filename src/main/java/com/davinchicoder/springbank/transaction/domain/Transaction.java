package com.davinchicoder.springbank.transaction.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Transaction {

    private String id;
    private String idempotencyKey;
    private Long version;
    private TransactionType type;
    @Builder.Default
    private TransactionStatus status = TransactionStatus.PENDING;
    private Instant timestamp;

}
