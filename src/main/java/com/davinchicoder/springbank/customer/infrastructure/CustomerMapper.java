package com.davinchicoder.springbank.customer.infrastructure;

import com.bank.customer.CustomerType;
import com.davinchicoder.springbank.common.MapperUtils;
import com.davinchicoder.springbank.customer.application.request.NewCustomerRequest;
import com.davinchicoder.springbank.customer.application.response.GetCustomerResponse;
import com.davinchicoder.springbank.customer.application.response.NewCustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, uses = MapperUtils.class)
public interface CustomerMapper {

    NewCustomerRequest toNewCustomerRequest(CustomerType customerType);

    CustomerType toCustomerType(NewCustomerResponse newCustomerResponse);

    CustomerType toCustomerType(GetCustomerResponse getCustomerResponse);
}
