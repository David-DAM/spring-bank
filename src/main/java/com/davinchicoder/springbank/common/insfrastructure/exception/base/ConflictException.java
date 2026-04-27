package com.davinchicoder.springbank.common.insfrastructure.exception.base;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
