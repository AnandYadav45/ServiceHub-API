package com.servicehub.amc.service;

import com.servicehub.amc.dto.AmcSubscriptionResponse;
import com.servicehub.amc.dto.AmcSubscriptionSaveRequest;

public interface AmcSubscriptionService {

    AmcSubscriptionResponse save(AmcSubscriptionSaveRequest request);
}
