package com.servicehub.common.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class AuthPropagationFilter {

    public static ExchangeFilterFunction propagate() {
        return (request, next) -> {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest currentRequest = attrs.getRequest();
                String authHeader = currentRequest.getHeader("Authorization");
                if (authHeader != null) {
                    ClientRequest mutated = ClientRequest.from(request).header("Authorization", authHeader).build();
                    return next.exchange(mutated);
                }
            }
            return next.exchange(request);
        };
    }
}
