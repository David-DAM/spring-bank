package com.davinchicoder.springbank.customer.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerQueryRepository extends JpaRepository<CustomerEntity, String> {
    
    Optional<CustomerEntity> findByEmail(String email);
}
