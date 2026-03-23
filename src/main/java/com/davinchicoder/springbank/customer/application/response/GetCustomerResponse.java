package com.davinchicoder.springbank.customer.application.response;

import com.davinchicoder.springbank.customer.domain.Customer;

import java.time.Instant;

public record GetCustomerResponse(String id, String name, String email, Instant createdAt) {

    public static GetCustomerResponse of(Customer customer) {
        return new GetCustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getCreatedAt());
    }

}
