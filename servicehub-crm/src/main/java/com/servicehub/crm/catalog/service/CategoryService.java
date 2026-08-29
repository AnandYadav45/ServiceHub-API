package com.servicehub.crm.catalog.service;

import com.servicehub.crm.catalog.dto.CategoryResponse;
import com.servicehub.crm.catalog.dto.CategorySaveRequest;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> findAll();
    CategoryResponse save(CategorySaveRequest request);
}
