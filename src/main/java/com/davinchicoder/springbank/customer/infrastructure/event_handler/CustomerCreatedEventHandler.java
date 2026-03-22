package com.davinchicoder.springbank.customer.infrastructure.event_handler;

import com.davinchicoder.springbank.common.domain.EventHandler;
import com.davinchicoder.springbank.customer.domain.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerCreatedEventHandler implements EventHandler {

    private final ObjectMapper objectMapper;

    @Override
    public String eventType() {
        return CustomerCreatedEvent.class.getSimpleName();
    }

    @Override
    public void handle(String payload) {
        CustomerCreatedEvent event = objectMapper.readValue(payload, CustomerCreatedEvent.class);
        log.info("Publishing customer: {}", event.customerId());
    }
}
