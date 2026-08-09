package com.servicehub.crm.lead.controller;

import com.servicehub.common.dto.ApiResponse;
import com.servicehub.crm.lead.dto.ServiceRequestResponse;
import com.servicehub.crm.lead.dto.ServiceRequestSaveRequest;
import com.servicehub.crm.lead.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leads/")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceRequestResponse>> save(@Valid @RequestBody ServiceRequestSaveRequest request) {
        ServiceRequestResponse response = serviceRequestService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }

}
