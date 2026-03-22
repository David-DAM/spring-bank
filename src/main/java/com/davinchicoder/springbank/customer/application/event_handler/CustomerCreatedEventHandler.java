package com.davinchicoder.springbank.customer.application.event_handler;

import com.davinchicoder.springbank.common.domain.EventHandler;
import com.davinchicoder.springbank.customer.domain.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerCreatedEventHandler implements EventHandler<CustomerCreatedEvent> {

    @Override
    public String eventType() {
        return CustomerCreatedEvent.class.getSimpleName();
    }

    @Override
    public Class<CustomerCreatedEvent> payloadType() {
        return CustomerCreatedEvent.class;
    }

    @Override
    public void handle(CustomerCreatedEvent event) {
        log.info("Publishing customer: {}", event.customerId());
    }
}
