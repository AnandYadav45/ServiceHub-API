package com.servicehub.commerce.order.entity;

import com.servicehub.commerce.product.entity.Product;
import com.servicehub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
@Setter @Getter
@NoArgsConstructor
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;   // same service — real relation

    @Column(nullable = false)
    private Integer quantity = 1;

    // Copied at order time on purpose — the "snapshot" pattern from earlier, so a later
    // price change on Product never silently rewrites the history of a past order.
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPriceSnapshot;
}
