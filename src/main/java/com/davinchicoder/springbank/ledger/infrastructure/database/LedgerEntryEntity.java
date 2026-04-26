package com.davinchicoder.springbank.ledger.infrastructure.database;

import com.davinchicoder.springbank.ledger.domain.EntryType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "ledger_entries")
public class LedgerEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String transactionId;
    private String accountId;
    private Long amount;
    @Enumerated(EnumType.STRING)
    private EntryType type;
    private Instant createdAt;

}
