package com.servicehub.amc.controller;


import com.servicehub.amc.dto.AmcSubscriptionResponse;
import com.servicehub.amc.dto.AmcSubscriptionSaveRequest;
import com.servicehub.amc.service.AmcSubscriptionService;
import com.servicehub.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/amc-subscriptions")
public class AmcSubscriptionController {

    private final AmcSubscriptionService subscriptionService;

    public AmcSubscriptionController(AmcSubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AmcSubscriptionResponse>> save(@Valid @RequestBody AmcSubscriptionSaveRequest request) {
        AmcSubscriptionResponse response = subscriptionService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }
}
