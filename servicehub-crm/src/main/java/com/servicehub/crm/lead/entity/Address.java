package com.servicehub.crm.lead.entity;

import com.servicehub.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter @Setter
@NoArgsConstructor
public class Address extends BaseEntity {
    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String addressLine1;

    private String area;

    @Column(nullable = false, length = 10)
    private String pincode;

    private Double latitude;
    private Double longitude;
}
