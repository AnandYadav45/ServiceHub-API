package com.servicehub.commerce.product.controller;


import com.servicehub.common.dto.ApiResponse;
import com.servicehub.commerce.product.dto.ProductResponse;
import com.servicehub.commerce.product.dto.ProductSaveRequest;
import com.servicehub.commerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> save(@Valid @RequestBody ProductSaveRequest request) {
        ProductResponse response = productService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }
}
