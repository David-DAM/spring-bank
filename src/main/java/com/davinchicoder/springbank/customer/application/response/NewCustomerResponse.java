package com.davinchicoder.springbank.customer.application.response;

import com.davinchicoder.springbank.customer.domain.Customer;

import java.time.Instant;


public record NewCustomerResponse(String id, String name, String email, Instant createdAt) {

    public static NewCustomerResponse of(Customer customer) {
        return new NewCustomerResponse(customer.getId(), customer.getName(), customer.getEmail(), customer.getCreatedAt());
    }

}
