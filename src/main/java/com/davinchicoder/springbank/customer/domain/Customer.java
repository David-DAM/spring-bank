package com.davinchicoder.springbank.customer.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Customer {

    private String id;

    private Long version;

    private String name;

    private String email;

    private Instant createdAt;

}
