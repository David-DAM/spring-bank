package com.davinchicoder.springbank.ledger.infrastructure;

import com.davinchicoder.springbank.ledger.domain.EntryType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
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
    @Column(precision = 19, scale = 4)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private EntryType type;
    private Instant createdAt;

}
