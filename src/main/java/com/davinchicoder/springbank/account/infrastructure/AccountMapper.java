package com.davinchicoder.springbank.account.infrastructure;

import com.davinchicoder.springbank.account.domain.Account;
import com.davinchicoder.springbank.common.insfrastructure.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, uses = MapperUtils.class)
public interface AccountMapper {

    Account toAccount(AccountEntity entity);

    AccountEntity toEntity(Account domain);
}
