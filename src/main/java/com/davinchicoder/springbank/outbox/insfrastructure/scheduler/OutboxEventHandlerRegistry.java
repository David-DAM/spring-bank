package com.davinchicoder.springbank.outbox.insfrastructure.scheduler;

import com.davinchicoder.springbank.common.domain.EventHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OutboxEventHandlerRegistry {

    private final Map<String, EventHandler> handlers;

    public OutboxEventHandlerRegistry(List<EventHandler> handlerList) {
        handlers = handlerList.stream()
                .collect(Collectors.toMap(EventHandler::eventType, Function.identity()));
    }

    public EventHandler get(String eventType) {
        EventHandler handler = handlers.get(eventType);
        if (handler == null) {
            throw new RuntimeException("No handler for event: " + eventType);
        }
        return handler;
    }
}
