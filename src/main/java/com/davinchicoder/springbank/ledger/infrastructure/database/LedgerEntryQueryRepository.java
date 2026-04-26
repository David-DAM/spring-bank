package com.davinchicoder.springbank.ledger.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerEntryQueryRepository extends JpaRepository<LedgerEntryEntity, String> {

    @Query(value = """
                SELECT COALESCE(SUM(
                    CASE 
                        WHEN type = 'CREDIT' THEN amount
                        WHEN type = 'DEBIT' THEN -amount
                    END
                ), 0)
                FROM ledger_entries
                WHERE account_id = :accountId
            """, nativeQuery = true)
    Long calculateBalanceByAccountId(@Param("accountId") String accountId);

    @Query(value = """
            SELECT transaction_id
            FROM ledger_entries
            GROUP BY transaction_id
            HAVING SUM(
                           CASE
                               WHEN type = 'CREDIT' THEN amount
                               WHEN type = 'DEBIT' THEN -amount
                               END
                   ) != 0;
            """, nativeQuery = true)
    List<String> findUnbalancedTransactions();
}
