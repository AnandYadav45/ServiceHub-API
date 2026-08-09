package com.servicehub.crm.lead.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class LeadResponse {
    private Long id;
    private String requestCode;
    private String status;
    private Long vendorId;
    private String vendorName;
    private String subCategoryName;
    private BigDecimal finalPrice;
}
