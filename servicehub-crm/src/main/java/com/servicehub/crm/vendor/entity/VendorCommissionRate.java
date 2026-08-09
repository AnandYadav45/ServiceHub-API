package com.servicehub.crm.vendor.entity;

import com.servicehub.common.entity.BaseEntity;
import com.servicehub.crm.catalog.entity.SubCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "")
@Setter @Getter
@NoArgsConstructor
public class VendorCommissionRate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;   // vendor and catalog are the same service now — real relation is correct

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal commissionValue;

    @Column(nullable = false)
    private LocalDate effectiveFrom = LocalDate.now();
}
