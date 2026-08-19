package com.servicehub.crm.vendor.controller;

import com.servicehub.common.dto.ApiResponse;
import com.servicehub.crm.vendor.dto.VendorResponse;
import com.servicehub.crm.vendor.dto.VendorSaveRequest;
import com.servicehub.crm.vendor.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/vendors")
public class VendorController {
    private final VendorService vendorService;   // depends on the INTERFACE, not VendorServiceImpl

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(vendorService.getById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<VendorResponse>> save(@Valid @RequestBody VendorSaveRequest request) {
        VendorResponse response = vendorService.save(request);
        HttpStatus status = request.id() == null ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(ApiResponse.success(response));
    }
}
