package com.davinchicoder.springbank.customer.application;

import java.time.Instant;


public record NewCustomerResponse(String id, String name, String email, Instant createdAt) {

}
