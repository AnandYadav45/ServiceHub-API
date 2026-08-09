package com.servicehub.commerce.mapper;

import com.servicehub.commerce.order.dto.OrderResponse;
import com.servicehub.commerce.order.entity.Order;
import com.servicehub.commerce.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productBrand", source = "product.brand")
    @Mapping(target = "productModel", source = "product.model")
    @Mapping(target = "unitPrice", source = "unitPriceSnapshot")
    OrderResponse.OrderItemResponse toItemResponse(OrderItem item);
}
