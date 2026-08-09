package com.servicehub.crm.catalog.service;

import com.servicehub.crm.catalog.dto.CategoryResponse;
import com.servicehub.crm.catalog.dto.CategorySaveRequest;

public interface CategoryService {

    CategoryResponse save(CategorySaveRequest request);
}
