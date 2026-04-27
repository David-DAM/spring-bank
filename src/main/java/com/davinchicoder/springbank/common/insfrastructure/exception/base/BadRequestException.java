package com.davinchicoder.springbank.common.insfrastructure.exception.base;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
