package com.davinchicoder.springbank.customer.application;

import com.davinchicoder.springbank.customer.application.request.NewCustomerRequest;
import com.davinchicoder.springbank.customer.application.response.GetCustomerResponse;
import com.davinchicoder.springbank.customer.application.response.NewCustomerResponse;
import com.davinchicoder.springbank.customer.domain.Customer;
import com.davinchicoder.springbank.customer.domain.CustomerCreatedEvent;
import com.davinchicoder.springbank.customer.infrastructure.repository.CustomerRepository;
import com.davinchicoder.springbank.outbox.infrastructure.database.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxRetries = 3
    )
    @Transactional
    public NewCustomerResponse createCustomer(NewCustomerRequest newCustomerRequest) {

        Customer customer = Customer.builder()
                .name(newCustomerRequest.name())
                .email(newCustomerRequest.email())
                .createdAt(newCustomerRequest.createdAt())
                .build();

        Customer saved = customerRepository.upsert(customer);

        outboxEventRepository.insertAll(List.of(CustomerCreatedEvent.of(saved)));

        return NewCustomerResponse.of(saved);
    }

    public GetCustomerResponse getCustomer(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        return GetCustomerResponse.of(customer);
    }

}
