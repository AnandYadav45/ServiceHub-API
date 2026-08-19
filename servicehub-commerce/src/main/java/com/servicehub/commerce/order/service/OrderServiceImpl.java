package com.servicehub.commerce.order.service;

import com.servicehub.commerce.mapper.OrderMapper;
import com.servicehub.commerce.order.dto.OrderResponse;
import com.servicehub.commerce.order.dto.OrderSaveRequest;
import com.servicehub.commerce.order.entity.Order;
import com.servicehub.commerce.order.entity.OrderItem;
import com.servicehub.commerce.order.repository.OrderRepository;
import com.servicehub.commerce.product.entity.Product;
import com.servicehub.commerce.product.enums.OrderStatus;
import com.servicehub.commerce.product.repository.ProductRepository;
import com.servicehub.common.config.CodeGeneratorUtil;
import com.servicehub.common.exceptions.BusinessValidationException;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }


    @Override
    @Transactional
    public OrderResponse save(OrderSaveRequest request) {
        Long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Order order;
        boolean isCreate = request.id() == null;

        if (isCreate) {
            order = new Order();
            order.setCustomerId(currentUserId);
            order.setOrderCode(CodeGeneratorUtil.generate("ORD"));
            order.setStatus(OrderStatus.PENDING);
        } else {
            order = orderRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "COMMERCE-404-002", "Order not found: " + request.id()));
            order.getItems().clear();   // orphanRemoval on the entity turns this into real DELETEs for the old rows
        }

        order.setDeliveryAddressId(request.deliveryAddressId());

        List<OrderItem> newItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderSaveRequest.OrderItemRequest itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "COMMERCE-404-001", "Product not found: " + itemRequest.productId()));

            if (product.getStockQuantity() < itemRequest.quantity()) {
                throw new BusinessValidationException(HttpStatus.BAD_REQUEST, "COMMERCE-400-001",
                        "Insufficient stock for product " + product.getId() + " — requested " + itemRequest.quantity()
                                + ", available " + product.getStockQuantity());
            }

            // Decremented inside the SAME transaction as the order — if anything later in this
            // method throws, the stock decrement rolls back with it, not left half-applied
            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPriceSnapshot(product.getPrice());   // captured now — never re-read later, even if price changes tomorrow
            newItems.add(item);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
        }

        order.getItems().addAll(newItems);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }
}
