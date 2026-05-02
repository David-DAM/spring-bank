package com.davinchicoder.springbank.account.domain;

import com.davinchicoder.springbank.audit.domain.AuditableDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class Account extends AuditableDomain {

    private String id;
    private Long version;
    private String iban;
    private Long balanceInCents;
    private AccountStatus status;

    public void validateCanOperate() {
        if (!AccountStatus.ACTIVE.equals(status)) {
            throw new IllegalStateException("Account not active");
        }
    }

}
