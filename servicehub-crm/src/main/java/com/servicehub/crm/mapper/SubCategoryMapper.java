package com.servicehub.crm.mapper;

import com.servicehub.crm.catalog.dto.SubCategoryResponse;
import com.servicehub.crm.catalog.dto.SubCategorySaveRequest;
import com.servicehub.crm.catalog.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)   // NOT resolvable by the mapper — see the service
    SubCategory toEntity(SubCategorySaveRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromRequest(SubCategorySaveRequest request, @MappingTarget SubCategory subCategory);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    SubCategoryResponse toResponse(SubCategory subCategory);
}
