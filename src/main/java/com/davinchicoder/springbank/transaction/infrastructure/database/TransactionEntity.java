package com.davinchicoder.springbank.transaction.infrastructure.database;

import com.davinchicoder.springbank.transaction.domain.TransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String idempotencyKey;
    @Version
    private Long version;
    private String accountNumber;
    private BigDecimal amount;
    private Instant timestamp;
    private TransactionType type;

}
