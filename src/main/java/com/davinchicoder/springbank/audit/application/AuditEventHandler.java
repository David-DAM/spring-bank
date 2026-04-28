package com.davinchicoder.springbank.audit.application;

import com.davinchicoder.springbank.audit.domain.AuditLogEvent;
import com.davinchicoder.springbank.audit.infrastructure.AuditRepository;
import com.davinchicoder.springbank.common.domain.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuditEventHandler implements EventHandler<AuditLogEvent> {

    private final AuditRepository auditRepository;

    @Override
    public String eventType() {
        return AuditLogEvent.class.getSimpleName();
    }

    @Override
    public Class<AuditLogEvent> payloadType() {
        return AuditLogEvent.class;
    }

    @Override
    public void handle(AuditLogEvent event) {
        log.info("Received AuditLogEvent: {}", event);
        auditRepository.insert(event);
    }
}
