package com.servicehub.amc.service;

import com.servicehub.amc.dto.AmcPlanResponse;
import com.servicehub.amc.dto.AmcPlanSaveRequest;

public interface AmcPlanService {

    AmcPlanResponse save(AmcPlanSaveRequest request);
}
