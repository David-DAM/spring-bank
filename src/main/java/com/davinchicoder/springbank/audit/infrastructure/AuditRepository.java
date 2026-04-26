package com.davinchicoder.springbank.audit.infrastructure;

import com.davinchicoder.springbank.common.domain.DomainEvent;
import com.davinchicoder.springbank.common.insfrastructure.TraceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class AuditRepository {

    private final ObjectMapper objectMapper;

    private final AuditQueryRepository repository;

    private final TraceUtils tracerUtils;

    public void insertAll(List<DomainEvent> events) {

        List<AuditEntity> entities = events.stream().map(event -> {

            AuditEntity auditEvent = new AuditEntity();
            auditEvent.setId(event.eventId());
            auditEvent.setEventType(event.getClass().getSimpleName());
            auditEvent.setPayload(objectMapper.writeValueAsString(event));
            auditEvent.setCorrelationId(tracerUtils.getTraceId());
            auditEvent.setCreatedAt(event.occurredAt());

            return auditEvent;
        }).toList();

        repository.saveAll(entities);
    }

}
