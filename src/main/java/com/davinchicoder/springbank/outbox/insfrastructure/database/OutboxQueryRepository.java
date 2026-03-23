package com.davinchicoder.springbank.outbox.insfrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxQueryRepository extends JpaRepository<OutboxEntity, String> {

    List<OutboxEntity> findTop100ByProcessedOrderByOccurredAt(boolean processed);

}
