package com.servicehub.commerce.product.entity;

import com.servicehub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
public class ProductImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String imageUrl;   // relative path — e.g. "products/abc123.jpg"

    @Column(nullable = false)
    private Integer displayOrder = 0;
}
