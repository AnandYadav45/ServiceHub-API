package com.servicehub.amc.service;

import com.servicehub.amc.dto.AmcSubscriptionResponse;
import com.servicehub.amc.dto.AmcSubscriptionSaveRequest;

import java.util.List;

public interface AmcSubscriptionService {

    List<AmcSubscriptionResponse> findAll();
    AmcSubscriptionResponse save(AmcSubscriptionSaveRequest request);
}
