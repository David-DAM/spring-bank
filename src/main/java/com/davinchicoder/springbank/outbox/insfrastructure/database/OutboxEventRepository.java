package com.davinchicoder.springbank.outbox.insfrastructure.database;

import com.davinchicoder.springbank.common.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class OutboxEventRepository {

    private final ObjectMapper objectMapper;

    private final Map<String, OutboxEntity> database = new ConcurrentHashMap<>();

    public List<OutboxEntity> findLastUnprocessed() {
        return database.values().stream()
                .sorted(Comparator.comparing(OutboxEntity::getOccurredAt))
                .filter(outboxEntity -> !outboxEntity.isProcessed())
                .limit(100)
                .toList();
    }

    public List<OutboxEntity> findLastProcessed() {
        return database.values().stream()
                .sorted(Comparator.comparing(OutboxEntity::getOccurredAt))
                .filter(OutboxEntity::isProcessed)
                .limit(100)
                .toList();
    }

    public void insertAll(List<DomainEvent> events) {

        events.forEach(domainEvent -> {

            OutboxEntity outboxEvent = OutboxEntity.builder()
                    .id(domainEvent.eventId())
                    .eventType(domainEvent.getClass().getSimpleName())
                    .payload(objectMapper.writeValueAsString(domainEvent))
                    .occurredAt(domainEvent.occurredAt())
                    .processed(false)
                    .build();

            database.computeIfAbsent(outboxEvent.getId(), _ -> outboxEvent);
        });

    }

    public void upsert(OutboxEntity event) {
        database.computeIfPresent(event.getId(), (_, _) -> event);
    }

    public void delete(OutboxEntity event) {
        database.remove(event.getId());
    }

}
