package com.servicehub.commerce.order.service;

import com.servicehub.commerce.order.dto.OrderResponse;
import com.servicehub.commerce.order.dto.OrderSaveRequest;

public interface OrderService {

    OrderResponse save(OrderSaveRequest request);
}
