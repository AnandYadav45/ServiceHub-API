package com.servicehub.commerce.product.service;

import com.servicehub.commerce.mapper.ProductMapper;
import com.servicehub.commerce.product.dto.ProductResponse;
import com.servicehub.commerce.product.dto.ProductSaveRequest;
import com.servicehub.commerce.product.entity.Product;
import com.servicehub.commerce.product.repository.ProductRepository;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl  implements ProductService{

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }


    @Override
    public ProductResponse save(ProductSaveRequest request) {
        Product product;
        if (request.id() != null) {
            product = productRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "COMMERCE-404-001", "Product not found: " + request.id()));
            productMapper.updateEntityFromRequest(request, product);
        } else {
            product = productMapper.toEntity(request);
        }
        Product saved = productRepository.save(product);
        return productMapper.toResponse(saved);
    }
}
