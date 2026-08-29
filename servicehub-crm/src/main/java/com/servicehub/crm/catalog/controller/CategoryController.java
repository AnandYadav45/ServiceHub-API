package com.servicehub.crm.catalog.controller;

import com.servicehub.common.dto.ApiResponse;
import com.servicehub.crm.catalog.dto.CategoryResponse;
import com.servicehub.crm.catalog.dto.CategorySaveRequest;
import com.servicehub.crm.catalog.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> save(@Valid @RequestBody CategorySaveRequest request) {
        CategoryResponse response = categoryService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> list() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findAll()));
    }
}
