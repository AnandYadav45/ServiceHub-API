package com.servicehub.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter extends OncePerRequestFilter {
    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // If an upstream caller (the gateway, or another service) already generated one,
        // reuse it — that's what makes the SAME id show up in multiple services' logs for
        // one logical user action, instead of a new id being minted at every hop.
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        try {
            MDC.put(MDC_KEY, correlationId);
            response.setHeader(CORRELATION_ID_HEADER, correlationId);
            chain.doFilter(request, response);
        } finally {
            // CRITICAL: application servers reuse threads from a pool. Without this, the NEXT
            // unrelated request handled by this same thread would silently inherit THIS
            // request's correlation id in its own log lines — a real, easy-to-miss bug.
            MDC.remove(MDC_KEY);
        }
    }
}
