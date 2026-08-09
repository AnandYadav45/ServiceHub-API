package com.servicehub.amc.mapper;


import com.servicehub.amc.dto.AmcPlanResponse;
import com.servicehub.amc.dto.AmcPlanSaveRequest;
import com.servicehub.amc.entity.AmcPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AmcPlanMapper {

    @Mapping(target = "id", ignore = true)
    AmcPlan toEntity(AmcPlanSaveRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(AmcPlanSaveRequest request, @MappingTarget AmcPlan amcPlan);

    AmcPlanResponse toResponse(AmcPlan amcPlan);
}