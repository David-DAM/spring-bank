package com.davinchicoder.springbank.transaction.infrastructure.database;

import com.davinchicoder.springbank.common.insfrastructure.MapperUtils;
import com.davinchicoder.springbank.transaction.domain.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, uses = MapperUtils.class)
public interface TransactionEntityMapper {

    Transaction toTransaction(TransactionEntity transactionEntity);

    TransactionEntity toTransactionEntity(Transaction transaction);

}
