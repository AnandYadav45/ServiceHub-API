package com.servicehub.crm.lead.entity;

import com.servicehub.common.entity.BaseEntity;
import com.servicehub.crm.vendor.entity.Vendor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lead_assignment")
@Setter @Getter
@NoArgsConstructor
public class LeadAssignment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_request_id", nullable = false)
    private ServiceRequest serviceRequest;   // same service — real relation

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;   // same service — real relation

    @Column(nullable = false)
    private Long assignedByUserId;   // the admin — lives in AUTH — plain ID again

    @Column(length = 20)
    private String vendorResponse;
}
