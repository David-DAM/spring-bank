package com.davinchicoder.springbank.account.infrastructure;

import com.davinchicoder.springbank.account.domain.AccountStatus;
import com.davinchicoder.springbank.audit.infrastructure.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class AccountEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Version
    private Long version;

    private String iban;

    private Long balanceInCents;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

}
