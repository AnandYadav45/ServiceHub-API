package com.servicehub.amc.service;

import com.servicehub.amc.dto.AmcPlanResponse;
import com.servicehub.amc.dto.AmcPlanSaveRequest;

import java.util.List;

public interface AmcPlanService {

    List<AmcPlanResponse> findAll();
    AmcPlanResponse save(AmcPlanSaveRequest request);
}
