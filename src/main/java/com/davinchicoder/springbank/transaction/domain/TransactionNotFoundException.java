package com.davinchicoder.springbank.transaction.domain;

import com.davinchicoder.springbank.common.insfrastructure.exception.base.NotFoundException;

public class TransactionNotFoundException extends NotFoundException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
