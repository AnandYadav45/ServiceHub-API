package com.servicehub.crm.vendor.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class VendorResponse {
    private Long id;
    private String businessName;
    private String phoneNumber;
    private String status;
    private BigDecimal avgRating;
}
