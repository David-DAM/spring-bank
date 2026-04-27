package com.davinchicoder.springbank.common.insfrastructure.exception.base;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
