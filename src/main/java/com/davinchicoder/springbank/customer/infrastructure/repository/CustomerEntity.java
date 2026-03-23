package com.davinchicoder.springbank.customer.infrastructure.repository;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Version
    private Long version;

    private String name;

    private String email;

    private Instant createdAt;

}
