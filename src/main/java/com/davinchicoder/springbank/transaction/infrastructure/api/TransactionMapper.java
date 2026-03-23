package com.davinchicoder.springbank.transaction.infrastructure.api;

import com.bank.transaction.TransactionType;
import com.davinchicoder.springbank.common.insfrastructure.MapperUtils;
import com.davinchicoder.springbank.transaction.application.NewTransactionRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, uses = MapperUtils.class)
public interface TransactionMapper {

    NewTransactionRequest toNewTransactionRequest(TransactionType transactionType);

}
