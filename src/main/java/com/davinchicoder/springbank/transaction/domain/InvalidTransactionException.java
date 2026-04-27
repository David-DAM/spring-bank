package com.davinchicoder.springbank.transaction.domain;

import com.davinchicoder.springbank.common.insfrastructure.exception.base.BadRequestException;

public class InvalidTransactionException extends BadRequestException {
    public InvalidTransactionException(String message) {
        super(message);
    }
}
