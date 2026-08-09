package com.servicehub.commerce.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import jakarta.validation.constraints.Positive;

public record OrderSaveRequest(
        Long id,
        @NotNull Long deliveryAddressId,
        @NotEmpty @Valid List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            @NotNull Long productId,
            @NotNull @Positive Integer quantity
    ) {}
}
