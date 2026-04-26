package com.davinchicoder.springbank.outbox.infrastructure.database;

import com.davinchicoder.springbank.common.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxEventRepository {

    private final ObjectMapper objectMapper;

    private final OutboxQueryRepository repository;

    public List<OutboxEntity> findLastUnprocessed() {

        return repository.lockNextBatch();
    }

    public void insertAll(List<DomainEvent> events) {

        List<OutboxEntity> entities = events.stream().map(domainEvent -> {

            OutboxEntity outboxEvent = new OutboxEntity();
            outboxEvent.setId(domainEvent.eventId());
            outboxEvent.setEventType(domainEvent.getClass().getSimpleName());
            outboxEvent.setPayload(objectMapper.writeValueAsString(domainEvent));
            outboxEvent.setOccurredAt(domainEvent.occurredAt());
            outboxEvent.setStatus(OutboxStatus.PENDING);

            return outboxEvent;
        }).toList();

        repository.saveAll(entities);
    }

    public void upsertAll(List<OutboxEntity> events) {
        repository.saveAll(events);
    }

    public void deleteAll(List<OutboxEntity> events) {
        repository.deleteAll(events);
    }

    public void deleteProcessedEvents() {
        Instant expiration = Instant.now().minus(7, ChronoUnit.DAYS);

        int deleted;
        do {
            deleted = repository.deleteBatch(expiration);
        } while (deleted > 0);
    }

}
