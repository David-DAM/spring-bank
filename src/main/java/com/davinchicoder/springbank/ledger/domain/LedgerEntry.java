package com.davinchicoder.springbank.ledger.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class LedgerEntry {
    private String id;
    private String transactionId;
    private String accountId;
    private Long amount;
    private EntryType type;
    private Instant createdAt;

    public void validateBalanced(List<LedgerEntry> entries) {
        long debit = entries.stream()
                .filter(e -> EntryType.DEBIT.equals(e.getType()))
                .map(LedgerEntry::getAmount)
                .reduce(0L, Long::sum);

        long credit = entries.stream()
                .filter(e -> EntryType.CREDIT.equals(e.getType()))
                .map(LedgerEntry::getAmount)
                .reduce(0L, Long::sum);

        if (debit != credit) {
            throw new IllegalStateException("Unbalanced transaction");
        }
    }

}
