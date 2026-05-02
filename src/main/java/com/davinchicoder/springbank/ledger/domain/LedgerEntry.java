package com.davinchicoder.springbank.ledger.domain;

import com.davinchicoder.springbank.audit.domain.AuditableDomain;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
public class LedgerEntry extends AuditableDomain {
    private String id;
    private String transactionId;
    private String accountId;
    private Long amount;
    private EntryType type;

}
