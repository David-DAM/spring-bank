package com.davinchicoder.springbank.account.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class Account {

    private String id;
    private String iban;
    private AccountStatus status;
    private Instant createdAt;

    public void validateCanOperate() {
        if (!AccountStatus.ACTIVE.equals(status)) {
            throw new IllegalStateException("Account not active");
        }
    }

}
