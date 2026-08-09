package com.servicehub.commerce.order.entity;

import com.servicehub.commerce.product.enums.OrderStatus;
import com.servicehub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String orderCode;

    @Column(nullable = false)
    private Long customerId;         // auth service — plain ID

    @Column(nullable = false)
    private Long deliveryAddressId;  // CRM service — plain ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

}
