package com.servicehub.crm.mapper;


import com.servicehub.crm.catalog.dto.CategoryResponse;
import com.servicehub.crm.catalog.dto.CategorySaveRequest;
import com.servicehub.crm.catalog.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toEntity(CategorySaveRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(CategorySaveRequest request, @MappingTarget Category category);

    CategoryResponse toResponse(Category category);
}
