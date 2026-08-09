package com.servicehub.crm.catalog.service;

import com.servicehub.crm.catalog.dto.SubCategoryResponse;
import com.servicehub.crm.catalog.dto.SubCategorySaveRequest;

public interface SubCategoryService {
    SubCategoryResponse save(SubCategorySaveRequest request);
}
