package com.servicehub.common.filter;

import com.servicehub.common.logging.CorrelationIdFilter;
import org.slf4j.MDC;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class CorrelationIdPropagationFilter {

    public static ExchangeFilterFunction propagate() {
        return (request, next) -> {
            String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);
            var mutated = correlationId != null
                    ? org.springframework.web.reactive.function.client.ClientRequest.from(request)
                    .header(CorrelationIdFilter.CORRELATION_ID_HEADER, correlationId).build()
                    : request;
            return next.exchange(mutated);
        };
    }
}
