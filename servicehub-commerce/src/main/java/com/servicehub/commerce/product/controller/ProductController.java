package com.servicehub.commerce.product.controller;


import com.servicehub.commerce.product.dto.ProductImageResponse;
import com.servicehub.common.dto.ApiResponse;
import com.servicehub.commerce.product.dto.ProductResponse;
import com.servicehub.commerce.product.dto.ProductSaveRequest;
import com.servicehub.commerce.product.service.ProductService;
import com.servicehub.common.dto.PagedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> save(@Valid @RequestBody ProductSaveRequest request) {
        ProductResponse response = productService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        int cappedSize = Math.min(size, 100);   // the cap flagged back in the pre-flight checklist — now actually enforced
        Page<ProductResponse> result = productService.list(PageRequest.of(page, cappedSize, Sort.by("createdAt").descending()));
        return ResponseEntity.ok(ApiResponse.success(com.servicehub.common.dto.PageMapper.from(result)));
    }




}
