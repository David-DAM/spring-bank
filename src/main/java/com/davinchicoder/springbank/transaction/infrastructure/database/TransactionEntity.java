package com.davinchicoder.springbank.transaction.infrastructure.database;

import com.davinchicoder.springbank.transaction.domain.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    private String id;
    private String accountNumber;
    private BigDecimal amount;
    private Instant timestamp;
    private TransactionType type;

}
