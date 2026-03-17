package com.davinchicoder.springbank.customer.application;

import com.davinchicoder.springbank.customer.domain.Customer;
import com.davinchicoder.springbank.customer.infrastructure.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public NewCustomerResponse createCustomer(NewCustomerRequest newCustomerRequest) {

        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name(newCustomerRequest.name())
                .email(newCustomerRequest.email())
                .createdAt(newCustomerRequest.createdAt())
                .build();

        Customer saved = customerRepository.save(customer);

        return new NewCustomerResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }

}
