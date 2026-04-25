package com.davinchicoder.springbank.outbox.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OutboxQueryRepository extends JpaRepository<OutboxEntity, String> {

    @Query(value = """
                SELECT *
                FROM outbox_events
                WHERE status = 'PENDING'
                ORDER BY occurred_at
                LIMIT 100
                FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    List<OutboxEntity> lockNextBatch();

    @Modifying
    @Query(value = """
                DELETE FROM outbox_events
                WHERE id IN (
                    SELECT id
                    FROM outbox_events
                    WHERE status = 'PROCESSED'
                      AND processed_at < :expirationTime
                    LIMIT 1000
                )
            """, nativeQuery = true)
    int deleteBatch(@Param("expirationTime") Instant expirationTime);

}
