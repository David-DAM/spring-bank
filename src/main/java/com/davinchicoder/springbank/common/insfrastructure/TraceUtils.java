package com.davinchicoder.springbank.common.insfrastructure;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TraceUtils {

    private final Tracer tracer;

    public String getTraceId() {
        Span span = tracer.currentSpan();
        
        return span != null ? span.context().traceId() : "";
    }
}
