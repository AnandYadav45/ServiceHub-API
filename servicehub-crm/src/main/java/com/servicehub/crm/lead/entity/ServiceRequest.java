package com.servicehub.crm.lead.entity;

import com.servicehub.common.entity.BaseEntity;
import com.servicehub.crm.catalog.entity.SubCategory;
import com.servicehub.crm.lead.enums.RequestStatus;
import com.servicehub.crm.vendor.entity.Vendor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "service_request")
@Setter @Getter
@NoArgsConstructor
public class ServiceRequest extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String requestCode;

    @Column(nullable = false)
    private Long customerId;   // auth service — plain ID, on purpose

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;   // same service as of the merge — real relation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_vendor_id")
    private Vendor assignedVendor;   // same service — real relation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id", nullable = false)
    private SubCategory subCategory;   // same service — real relation

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestStatus status = RequestStatus.NEW;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal quotedPrice;
    private BigDecimal finalPrice;
}
