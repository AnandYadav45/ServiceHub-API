package com.servicehub.amc.mapper;

import com.servicehub.amc.dto.AmcSubscriptionResponse;
import com.servicehub.amc.entity.AmcSubscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AmcSubscriptionMapper {
    @Mapping(target = "amcPlanId", source = "amcPlan.id")
    @Mapping(target = "planName", source = "amcPlan.name")
    @Mapping(target = "includedVisits", source = "amcPlan.includedVisits")
    AmcSubscriptionResponse toResponse(AmcSubscription subscription);
}
