package com.servicehub.crm.lead.service;

import com.servicehub.crm.lead.dto.ServiceRequestResponse;
import com.servicehub.crm.lead.dto.ServiceRequestSaveRequest;

public interface ServiceRequestService {

    ServiceRequestResponse save(ServiceRequestSaveRequest request);
}
