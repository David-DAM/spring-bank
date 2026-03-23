package com.davinchicoder.springbank.transaction.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionQueryRepository extends JpaRepository<TransactionEntity, String> {

}
