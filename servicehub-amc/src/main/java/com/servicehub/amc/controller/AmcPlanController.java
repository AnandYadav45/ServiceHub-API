package com.servicehub.amc.controller;

import com.servicehub.amc.dto.AmcPlanResponse;
import com.servicehub.amc.dto.AmcPlanSaveRequest;
import com.servicehub.amc.service.AmcPlanService;
import com.servicehub.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/amc-plans")
@PreAuthorize("hasRole('ADMIN')")
public class AmcPlanController {

    private final AmcPlanService amcPlanService;

    public AmcPlanController(AmcPlanService amcPlanService) {
        this.amcPlanService = amcPlanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AmcPlanResponse>> save(@Valid @RequestBody AmcPlanSaveRequest request) {
        AmcPlanResponse response = amcPlanService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }
}