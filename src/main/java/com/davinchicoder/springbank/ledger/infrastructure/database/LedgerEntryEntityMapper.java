package com.davinchicoder.springbank.ledger.infrastructure.database;

import com.davinchicoder.springbank.common.insfrastructure.MapperUtils;
import com.davinchicoder.springbank.ledger.domain.LedgerEntry;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR, uses = MapperUtils.class)
public interface LedgerEntryEntityMapper {

    LedgerEntry toLedgerEntry(LedgerEntryEntity entity);

    LedgerEntryEntity toLedgerEntryEntity(LedgerEntry ledgerEntry);
}
