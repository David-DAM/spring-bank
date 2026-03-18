package com.davinchicoder.springbank.customer.application.response;

import java.time.Instant;

public record GetCustomerResponse(String id, String name, String email, Instant createdAt) {

}
