package com.davinchicoder.springbank.outbox.insfrastructure.database;

import com.davinchicoder.springbank.common.domain.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxEventRepository {

    private final ObjectMapper objectMapper;

    private final OutboxQueryRepository outboxQueryRepository;

    public List<OutboxEntity> findLastUnprocessed() {

        return outboxQueryRepository.findTop100ByProcessedOrderByOccurredAt(false);
    }

    public List<OutboxEntity> findLastProcessed() {
        return outboxQueryRepository.findTop100ByProcessedOrderByOccurredAt(true);
    }

    public void insertAll(List<DomainEvent> events) {

        List<OutboxEntity> entities = events.stream().map(domainEvent -> {

            OutboxEntity outboxEvent = new OutboxEntity();
            outboxEvent.setId(domainEvent.eventId());
            outboxEvent.setEventType(domainEvent.getClass().getSimpleName());
            outboxEvent.setPayload(objectMapper.writeValueAsString(domainEvent));
            outboxEvent.setOccurredAt(domainEvent.occurredAt());
            outboxEvent.setProcessed(false);

            return outboxEvent;
        }).toList();

        outboxQueryRepository.saveAll(entities);

    }

    public void upsert(OutboxEntity event) {
        outboxQueryRepository.save(event);
    }

    public void delete(OutboxEntity event) {
        outboxQueryRepository.delete(event);
    }

}
