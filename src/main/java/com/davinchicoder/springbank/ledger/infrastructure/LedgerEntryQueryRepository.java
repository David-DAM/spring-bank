package com.davinchicoder.springbank.ledger.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerEntryQueryRepository extends JpaRepository<LedgerEntryEntity, String> {

    @Query(value = """
                SELECT COALESCE(SUM(
                    CASE 
                        WHEN e.type = 'CREDIT' THEN e.amount
                        WHEN e.type = 'DEBIT' THEN -e.amount
                    END
                ), 0)
                FROM ledger_entries e
                WHERE e.account_id = :accountId
            """, nativeQuery = true)
    Long sumByAccountId(@Param("accountId") String accountId);
}
