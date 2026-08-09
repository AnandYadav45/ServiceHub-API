package com.servicehub.crm.catalog.controller;

import com.servicehub.common.dto.ApiResponse;
import com.servicehub.crm.catalog.dto.SubCategoryResponse;
import com.servicehub.crm.catalog.dto.SubCategorySaveRequest;
import com.servicehub.crm.catalog.service.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin//subcategories")
@PreAuthorize("hasRole('ADMIN')")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SubCategoryResponse>> save(@Valid @RequestBody SubCategorySaveRequest request) {
        SubCategoryResponse response = subCategoryService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }
}
