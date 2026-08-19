package com.servicehub.commerce.product.controller;

import com.servicehub.commerce.product.dto.ProductImageResponse;
import com.servicehub.commerce.product.service.ProductService;
import com.servicehub.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
public class ProductImageController {

    private final ProductService productService;

    public ProductImageController(ProductService productImageService) {
        this.productService = productImageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{productId}/images", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProductImageResponse>> upload(@PathVariable Long productId,
                                                                    @RequestParam("file") MultipartFile file) {
        ProductImageResponse response = productService.upload(productId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> list(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getByProduct(productId)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long imageId) {
        productService.delete(imageId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
