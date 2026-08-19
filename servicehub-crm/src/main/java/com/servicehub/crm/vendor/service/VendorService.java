package com.servicehub.crm.vendor.service;

import com.servicehub.crm.vendor.dto.VendorResponse;
import com.servicehub.crm.vendor.dto.VendorSaveRequest;

import java.math.BigDecimal;
import java.util.List;

public interface VendorService {
    VendorResponse getById(Long id);
    VendorResponse save(VendorSaveRequest request);
    List<VendorResponse> findTopRated(BigDecimal minRating);
}
