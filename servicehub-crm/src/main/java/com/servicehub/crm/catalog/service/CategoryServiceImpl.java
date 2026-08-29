package com.servicehub.crm.catalog.service;

import com.servicehub.common.exceptions.ResourceNotFoundException;
import com.servicehub.crm.catalog.dto.CategoryResponse;
import com.servicehub.crm.catalog.dto.CategorySaveRequest;
import com.servicehub.crm.catalog.entity.Category;
import com.servicehub.crm.catalog.repository.CategoryRepository;
import com.servicehub.crm.mapper.CategoryMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponse> findAll() {
        return List.of();
    }

    @Override
    @Transactional
    public CategoryResponse save(CategorySaveRequest request) {
        Category category;
        if (request.id() != null) {
            category = categoryRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-002", "Category not found:" + request.id()));
            categoryMapper.updateEntityFromRequest(request, category);
        } else {
            category = categoryMapper.toEntity(request);
        }
        Category saved = categoryRepository.save(category);
        return categoryMapper.toResponse(saved);
    }
}
