package com.davinchicoder.springbank.transaction.application.event_handler;

import com.davinchicoder.springbank.common.domain.EventHandler;
import com.davinchicoder.springbank.transaction.domain.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionCreatedEventHandler implements EventHandler<TransactionCreatedEvent> {

    @Override
    public String eventType() {
        return TransactionCreatedEvent.class.getSimpleName();
    }

    @Override
    public Class<TransactionCreatedEvent> payloadType() {
        return TransactionCreatedEvent.class;
    }

    @Override
    public void handle(TransactionCreatedEvent event) {
        log.info("Publishing transaction: {}", event.customerId());
    }
}
