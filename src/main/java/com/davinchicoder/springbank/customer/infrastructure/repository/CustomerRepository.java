package com.davinchicoder.springbank.customer.infrastructure.repository;

import com.davinchicoder.springbank.customer.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final CustomerQueryRepository customerQueryRepository;
    private final CustomerEntityMapper customerEntityMapper;

    public Optional<Customer> findById(String id) {
        return customerQueryRepository.findById(id).map(customerEntityMapper::toCustomer);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerQueryRepository.findByEmail(email).map(customerEntityMapper::toCustomer);
    }

    public Customer upsert(Customer customer) {
        CustomerEntity entity = customerEntityMapper.toCustomerEntity(customer);

        CustomerEntity saved = customerQueryRepository.save(entity);

        return customerEntityMapper.toCustomer(saved);
    }
}
