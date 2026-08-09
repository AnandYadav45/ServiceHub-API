package com.servicehub.commerce.product.service;

import com.servicehub.commerce.product.dto.ProductResponse;
import com.servicehub.commerce.product.dto.ProductSaveRequest;

public interface ProductService {

    ProductResponse save(ProductSaveRequest request);
}
