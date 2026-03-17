package com.davinchicoder.springbank.customer.infrastructure;

import com.bank.customer.CustomerType;
import com.davinchicoder.springbank.customer.application.CustomerService;
import com.davinchicoder.springbank.customer.application.NewCustomerRequest;
import com.davinchicoder.springbank.customer.application.NewCustomerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public CustomerType createCustomer(@RequestBody CustomerType customerType) {
        NewCustomerRequest request = customerMapper.toNewCustomerRequest(customerType);

        NewCustomerResponse response = customerService.createCustomer(request);

        return customerMapper.toCustomerType(response);
    }

}
