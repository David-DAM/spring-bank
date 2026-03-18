package com.davinchicoder.springbank.customer.infrastructure;

import com.bank.customer.CustomerType;
import com.bank.customer.ObjectFactory;
import com.davinchicoder.springbank.customer.application.CustomerService;
import com.davinchicoder.springbank.customer.application.request.NewCustomerRequest;
import com.davinchicoder.springbank.customer.application.response.GetCustomerResponse;
import com.davinchicoder.springbank.customer.application.response.NewCustomerResponse;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public JAXBElement<CustomerType> createCustomer(@RequestBody CustomerType customerType) {
        NewCustomerRequest request = customerMapper.toNewCustomerRequest(customerType);

        NewCustomerResponse response = customerService.createCustomer(request);

        CustomerType responseType = customerMapper.toCustomerType(response);

        return new ObjectFactory().createCustomer(responseType);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public JAXBElement<CustomerType> getCustomer(@PathVariable String id) {
        GetCustomerResponse response = customerService.getCustomer(id);

        CustomerType responseType = customerMapper.toCustomerType(response);

        return new ObjectFactory().createCustomer(responseType);
    }

}
