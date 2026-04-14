package com.davinchicoder.springbank.ledger.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerEntryQueryRepository extends JpaRepository<LedgerEntryEntity, String> {

}
