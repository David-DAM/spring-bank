package com.davinchicoder.springbank.ledger.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class LedgerEntry {
    private String id;
    private String transactionId;
    private String accountId;
    private BigDecimal amount;
    private EntryType type;
    private Instant createdAt;

}
