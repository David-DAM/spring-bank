package com.davinchicoder.springbank.audit.infrastructure;

import com.davinchicoder.springbank.audit.domain.AuditLogEvent;
import com.davinchicoder.springbank.common.insfrastructure.TraceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AuditRepository {

    private final AuditQueryRepository repository;

    private final TraceUtils tracerUtils;

    public void insert(AuditLogEvent event) {

        AuditEntity entity = new AuditEntity();
        entity.setId(event.eventId());
        entity.setEventType(event.eventType());
        entity.setPayload(event.domainEvent());
        entity.setCorrelationId(tracerUtils.getTraceId());
        entity.setCreatedAt(event.occurredAt());

        repository.save(entity);
    }

}
