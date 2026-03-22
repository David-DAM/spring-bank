package com.davinchicoder.springbank.outbox.insfrastructure.scheduler;

import com.davinchicoder.springbank.common.domain.EventHandler;
import com.davinchicoder.springbank.outbox.insfrastructure.database.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OutboxScheduler {

    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventHandlerRegistry outboxEventHandlerRegistry;

    @Scheduled(cron = "*/15 * * * * *")
    public void findAllUnprocessedEvents() {
        outboxEventRepository.findLastUnprocessed().forEach(outboxEvent -> {
            try {

                EventHandler handler = outboxEventHandlerRegistry.get(outboxEvent.getEventType());

                handler.handle(outboxEvent.getPayload());

                outboxEvent.setProcessed(true);

                outboxEventRepository.upsert(outboxEvent);
            } catch (Exception e) {
                log.error("Error processing event: {}", outboxEvent.getId(), e);
            }
        });

    }

    @Scheduled(cron = "0 0 * * * *")
    public void clearProcessedEvents() {

        outboxEventRepository.findLastProcessed().forEach(outboxEvent -> {
            try {
                outboxEventRepository.delete(outboxEvent);

            } catch (Exception e) {
                log.error("Error processing event: {}", outboxEvent.getId(), e);
            }
        });

    }

}
