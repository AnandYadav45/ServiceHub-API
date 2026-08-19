package com.servicehub.commerce.product.service;

import com.servicehub.commerce.mapper.ProductMapper;
import com.servicehub.commerce.product.dto.ProductImageResponse;
import com.servicehub.commerce.product.dto.ProductResponse;
import com.servicehub.commerce.product.dto.ProductSaveRequest;
import com.servicehub.commerce.product.entity.Product;
import com.servicehub.commerce.product.entity.ProductImage;
import com.servicehub.commerce.product.repository.ProductImageRepository;
import com.servicehub.commerce.product.repository.ProductRepository;
import com.servicehub.common.exceptions.BusinessValidationException;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
public class ProductServiceImpl  implements ProductService{
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/webp");


    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductImageRepository productImageRepository;
    private final FileStorageService fileStorageService;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                              ProductImageRepository productImageRepository, FileStorageService fileStorageService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productImageRepository = productImageRepository;
        this.fileStorageService = fileStorageService;
    }


    @Override
    @Transactional
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

    @Override
    @Transactional
    public ProductImageResponse upload(Long productId, MultipartFile file) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "COMMERCE-404-001", "Product not found: " + productId));

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BusinessValidationException(HttpStatus.BAD_REQUEST, "COMMERCE-400-002", "Only JPEG, PNG, or WebP images are allowed");
        }

        String relativePath = fileStorageService.store(file, "products");

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setImageUrl(relativePath);
        image.setDisplayOrder(productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId).size());

        ProductImage saved = productImageRepository.save(image);
        return new ProductImageResponse(saved.getId(), "/uploads/" + saved.getImageUrl(), saved.getDisplayOrder());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageResponse> getByProduct(Long productId) {
        return productImageRepository.findByProductIdOrderByDisplayOrderAsc(productId).stream()
                .map(img -> new ProductImageResponse(img.getId(), "/uploads/" + img.getImageUrl(), img.getDisplayOrder()))
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "COMMERCE-404-003", "Image not found: " + imageId));
        fileStorageService.delete(image.getImageUrl());
        productImageRepository.delete(image);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "COMMERCE-404-001", "Product not found: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> list(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toResponse);
    }
}
