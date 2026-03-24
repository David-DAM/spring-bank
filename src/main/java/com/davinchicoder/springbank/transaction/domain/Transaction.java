package com.davinchicoder.springbank.transaction.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class Transaction {

    private String id;
    private String idempotencyKey;
    private Long version;
    private String accountNumber;
    private BigDecimal amount;
    private Instant timestamp;
    private TransactionType type;

}
