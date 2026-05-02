package com.davinchicoder.springbank.ledger.infrastructure.database;

import com.davinchicoder.springbank.audit.infrastructure.AuditableEntity;
import com.davinchicoder.springbank.ledger.domain.EntryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ledger_entries")
public class LedgerEntryEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String transactionId;

    private String accountId;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private EntryType type;

}
