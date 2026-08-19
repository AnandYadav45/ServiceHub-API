package com.servicehub.api.gateway.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class GatewayRequestLoggingFilter implements WebFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger("com.servicehub.access");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        long start = System.currentTimeMillis();
        return chain.filter(exchange).doFinally(signalType -> {
            long durationMs = System.currentTimeMillis() - start;
            log.info("{} {} -> {} ({} ms)",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI().getPath(),
                    exchange.getResponse().getStatusCode(),
                    durationMs);
        });
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
