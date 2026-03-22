package com.davinchicoder.springbank.common.domain;

public interface EventHandler {

    String eventType();

    void handle(String payload);
}
