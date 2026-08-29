package com.servicehub.crm.catalog.service;

import com.servicehub.crm.catalog.dto.SubCategoryResponse;
import com.servicehub.crm.catalog.dto.SubCategorySaveRequest;

import java.util.List;

public interface SubCategoryService {
    List<SubCategoryResponse> findAll();
    List<SubCategoryResponse> findByCategoryId(Long categoryId);
    SubCategoryResponse save(SubCategorySaveRequest request);
}
