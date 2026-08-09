package com.servicehub.commerce.mapper;

import com.servicehub.commerce.product.dto.ProductResponse;
import com.servicehub.commerce.product.dto.ProductSaveRequest;
import com.servicehub.commerce.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    Product toEntity(ProductSaveRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(ProductSaveRequest request, @MappingTarget Product product);

    ProductResponse toResponse(Product product);
}
