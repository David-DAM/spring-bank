package com.davinchicoder.springbank.outbox.infrastructure.scheduler;

import com.davinchicoder.springbank.common.domain.DomainEvent;
import com.davinchicoder.springbank.common.domain.EventHandler;
import com.davinchicoder.springbank.outbox.infrastructure.database.OutboxEntity;
import com.davinchicoder.springbank.outbox.infrastructure.database.OutboxEventRepository;
import com.davinchicoder.springbank.outbox.infrastructure.database.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OutboxScheduler {

    private final OutboxEventRepository repository;
    private final OutboxEventHandlerRegistry handlerRegistry;
    private final ObjectMapper mapper;

    @Scheduled(cron = "*/5 * * * * *")
    public void findAllUnprocessedEvents() {

        List<OutboxEntity> unprocessedEvents = repository.findLastUnprocessed();

        unprocessedEvents.forEach(event -> {
            try {

                EventHandler<DomainEvent> handler = (EventHandler<DomainEvent>) handlerRegistry.get(event.getEventType());

                DomainEvent domainEvent = mapper.readValue(event.getPayload(), handler.payloadType());

                handler.handle(domainEvent);

                event.setStatus(OutboxStatus.PROCESSED);
                event.setProcessedAt(Instant.now());

            } catch (Exception e) {
                log.error("Error processing event: {}", event.getId(), e);
                event.setRetryCount(event.getRetryCount() + 1);
                event.setLastAttemptAt(Instant.now());
                event.setErrorMessage(e.getMessage());

                if (event.getRetryCount() >= 3) {
                    event.setStatus(OutboxStatus.FAILED);
                } else {
                    event.setStatus(OutboxStatus.PENDING);
                }
            }
        });

        repository.upsertAll(unprocessedEvents);

    }

    @Scheduled(cron = "0 0 * * * *")
    public void clearProcessedEvents() {
        repository.deleteProcessedEvents();
    }

}
