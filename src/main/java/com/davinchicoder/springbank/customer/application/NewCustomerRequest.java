package com.davinchicoder.springbank.customer.application;

import java.time.Instant;


public record NewCustomerRequest(String name, String email, Instant createdAt) {

}
