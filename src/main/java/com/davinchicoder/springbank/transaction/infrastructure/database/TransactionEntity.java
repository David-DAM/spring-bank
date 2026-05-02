package com.davinchicoder.springbank.transaction.infrastructure.database;

import com.davinchicoder.springbank.audit.infrastructure.AuditableEntity;
import com.davinchicoder.springbank.transaction.domain.TransactionStatus;
import com.davinchicoder.springbank.transaction.domain.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class TransactionEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String idempotencyKey;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
