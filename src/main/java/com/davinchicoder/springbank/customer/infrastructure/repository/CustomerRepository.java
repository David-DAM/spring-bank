package com.davinchicoder.springbank.customer.infrastructure.repository;

import com.davinchicoder.springbank.customer.domain.Customer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {

    private final Map<String, Customer> customers;

    public CustomerRepository() {
        this.customers = new ConcurrentHashMap<>();
    }

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(customers.get(id));
    }

    public Optional<Customer> findByEmail(String email) {
        return customers.values().stream().filter(c -> c.getEmail().equals(email)).findFirst();
    }

    public Customer save(Customer customer) {
        return customers.computeIfAbsent(customer.getId(), _ -> customer);
    }

    public Customer update(Customer customer) {
        return customers.computeIfPresent(customer.getId(), (_, _) -> customer);
    }

    public void remove(String id) {
        customers.remove(id);
    }
}
