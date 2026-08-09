package com.servicehub.amc.entity;


import com.servicehub.amc.enums.AmcStatus;
import com.servicehub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "amc_subcriptions")
@Getter
@Setter
@NoArgsConstructor
public class AmcSubscription extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String subscriptionCode;

    @Column(nullable = false)
    private Long customerId;   // auth service — plain ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amc_plan_id", nullable = false)
    private AmcPlan amcPlan;   // same service — real relation

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer visitsUsed = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AmcStatus status = AmcStatus.ACTIVE;
}
