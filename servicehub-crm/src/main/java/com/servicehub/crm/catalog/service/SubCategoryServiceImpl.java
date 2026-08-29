package com.servicehub.crm.catalog.service;

import com.servicehub.common.exceptions.ResourceNotFoundException;
import com.servicehub.crm.catalog.dto.SubCategoryResponse;
import com.servicehub.crm.catalog.dto.SubCategorySaveRequest;
import com.servicehub.crm.catalog.entity.Category;
import com.servicehub.crm.catalog.entity.SubCategory;
import com.servicehub.crm.catalog.repository.CategoryRepository;
import com.servicehub.crm.catalog.repository.SubCategoryRepository;
import com.servicehub.crm.mapper.SubCategoryMapper;
import org.springframework.http.HttpStatus;

import java.util.List;

public class SubCategoryServiceImpl implements SubCategoryService{

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository, CategoryRepository categoryRepository, SubCategoryMapper subCategoryMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryMapper = subCategoryMapper;
    }

    @Override
    public List<SubCategoryResponse> findAll() {
        return List.of();
    }

    @Override
    public List<SubCategoryResponse> findByCategoryId(Long categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId).stream().map(subCategoryMapper::toResponse).toList();
    }

    @Override
    public SubCategoryResponse save(SubCategorySaveRequest request) {
        // The lookup the mapper couldn't do on its own
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-002", "Category not found: " + request.categoryId()));

        SubCategory subCategory;
        if (request.id() != null) {
            subCategory = subCategoryRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND,"CRM-404-003", "Sub-category not found: " + request.id()));
            subCategoryMapper.updateEntityFromRequest(request, subCategory);
        } else {
            subCategory = subCategoryMapper.toEntity(request);
        }

        subCategory.setCategory(category);   // the one line the mapper structurally couldn't do for us

        SubCategory saved = subCategoryRepository.save(subCategory);
        return subCategoryMapper.toResponse(saved);
    }
}
