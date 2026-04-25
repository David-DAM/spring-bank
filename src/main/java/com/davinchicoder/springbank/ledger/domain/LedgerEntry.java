package com.davinchicoder.springbank.ledger.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class LedgerEntry {
    private String id;
    private String transactionId;
    private String accountId;
    private BigDecimal amount;
    private EntryType type;
    private Instant createdAt;

    public void validateBalanced(List<LedgerEntry> entries) {
        BigDecimal debit = entries.stream()
                .filter(e -> EntryType.DEBIT.equals(e.getType()))
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal credit = entries.stream()
                .filter(e -> EntryType.CREDIT.equals(e.getType()))
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!debit.equals(credit)) {
            throw new IllegalStateException("Unbalanced transaction");
        }
    }

}
