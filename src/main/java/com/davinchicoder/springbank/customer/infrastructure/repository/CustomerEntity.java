package com.davinchicoder.springbank.customer.infrastructure.repository;

import com.davinchicoder.springbank.audit.infrastructure.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class CustomerEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Version
    private Long version;

    private String name;

    private String email;

}
