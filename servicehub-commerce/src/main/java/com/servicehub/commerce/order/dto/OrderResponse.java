package com.servicehub.commerce.order.dto;

import java.math.BigDecimal;
import java.util.List;


public record OrderResponse(
        Long id,
        String orderCode,
        Long customerId,
        Long deliveryAddressId,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items
) {
    public record OrderItemResponse(
            Long productId,
            String productBrand,
            String productModel,
            Integer quantity,
            BigDecimal unitPrice
    ) {}
}