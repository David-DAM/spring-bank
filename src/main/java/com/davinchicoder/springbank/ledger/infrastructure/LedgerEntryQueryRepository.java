package com.davinchicoder.springbank.ledger.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface LedgerEntryQueryRepository extends JpaRepository<LedgerEntryEntity, String> {

    @Query(value = "SELECT SUM(e.amount) FROM ledger_entries e WHERE e.account_id = :accountId", nativeQuery = true)
    BigDecimal sumByAccountId(@Param("accountId") String accountId);
}
