package com.servicehub.crm.mapper;

import com.servicehub.crm.vendor.dto.VendorResponse;
import com.servicehub.crm.vendor.dto.VendorSaveRequest;
import com.servicehub.crm.vendor.dto.VendorSummaryDto;
import com.servicehub.crm.vendor.entity.Vendor;
import org.mapstruct.Mapping;

public interface VendorMapper {

    @Mapping(target = "id", ignore = true)
    Vendor toEntity(VendorSaveRequest request);
    VendorResponse toResponse(Vendor vendor);
    VendorSummaryDto toSummaryDto(Vendor vendor);
}
