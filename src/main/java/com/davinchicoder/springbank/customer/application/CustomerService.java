package com.davinchicoder.springbank.customer.application;

import com.davinchicoder.springbank.customer.application.request.NewCustomerRequest;
import com.davinchicoder.springbank.customer.application.response.GetCustomerResponse;
import com.davinchicoder.springbank.customer.application.response.NewCustomerResponse;
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

    public GetCustomerResponse getCustomer(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        return new GetCustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getCreatedAt()
        );
    }

}
