package com.servicehub.api.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicehub.common.dto.ApiError;
import com.servicehub.common.dto.ApiResponse;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GatewayErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GatewayErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        // Most uncaught errors reaching here mean the gateway couldn't complete the proxy call —
        // downstream service down, connection refused, timeout. 503 is the honest status for that.
        HttpStatus status = (ex instanceof ResponseStatusException rse)
                ? HttpStatus.valueOf(rse.getStatusCode().value())
                : HttpStatus.SERVICE_UNAVAILABLE;

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<Void> body = ApiResponse.error(new ApiError("GATEWAY-" + status.value() + "-001", "Unable to process request", null));

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (Exception e) {
            bytes = "{}".getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
