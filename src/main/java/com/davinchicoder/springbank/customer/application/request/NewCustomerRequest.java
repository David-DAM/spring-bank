package com.davinchicoder.springbank.customer.application.request;

import java.time.Instant;


public record NewCustomerRequest(String name, String email, Instant createdAt) {

}
