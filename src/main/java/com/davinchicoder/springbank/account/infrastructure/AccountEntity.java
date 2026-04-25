package com.davinchicoder.springbank.account.infrastructure;

import com.davinchicoder.springbank.account.domain.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String iban;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private Instant createdAt;

}
