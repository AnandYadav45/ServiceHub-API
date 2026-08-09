package com.servicehub.api.gateway.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitFilter  implements GlobalFilter, Ordered {
    private static final int MAX_REQUESTS_PER_WINDOW = 5;
    private static final String[] LIMITED_PATH_PREFIXES = {
            "/api/v1/auth/login",
            "/api/v1/auth/otp",
            "/api/v1/auth/forgot-password"
    };

    // Key: caller IP. Each entry expires after 1 minute, which resets that IP's count —
    // a simple fixed-window limiter, entirely in-memory.
    private final Cache<String, AtomicInteger> requestCounts = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(50_000)
            .build();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        boolean limited = false;
        for (String prefix : LIMITED_PATH_PREFIXES) {
            if (path.startsWith(prefix)) { limited = true; break; }
        }
        if (!limited) {
            return chain.filter(exchange);
        }

        String clientIp = exchange.getRequest().getRemoteAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";

        AtomicInteger count = requestCounts.get(clientIp, ip -> new AtomicInteger(0));
        if (count.incrementAndGet() > MAX_REQUESTS_PER_WINDOW) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;   // runs before routing
    }
}
