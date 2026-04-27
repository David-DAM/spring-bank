package com.davinchicoder.springbank.account.domain;

import com.davinchicoder.springbank.common.insfrastructure.exception.base.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
