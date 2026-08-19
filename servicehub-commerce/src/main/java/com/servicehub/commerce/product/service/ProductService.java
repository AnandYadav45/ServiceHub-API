package com.servicehub.commerce.product.service;

import com.servicehub.commerce.product.dto.ProductImageResponse;
import com.servicehub.commerce.product.dto.ProductResponse;
import com.servicehub.commerce.product.dto.ProductSaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponse save(ProductSaveRequest request);
    ProductImageResponse upload(Long productId, MultipartFile file);
    List<ProductImageResponse> getByProduct(Long productId);
    void delete(Long imageId);


    ProductResponse getById(Long id);
    Page<ProductResponse> list(Pageable pageable);
}
