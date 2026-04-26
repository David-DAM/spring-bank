package com.davinchicoder.springbank.audit.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditQueryRepository extends JpaRepository<AuditEntity, String> {


}
