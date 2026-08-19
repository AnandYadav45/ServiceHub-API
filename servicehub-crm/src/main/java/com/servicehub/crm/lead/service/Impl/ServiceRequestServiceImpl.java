package com.servicehub.crm.lead.service.Impl;

import com.servicehub.common.config.CodeGeneratorUtil;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import com.servicehub.crm.catalog.entity.SubCategory;
import com.servicehub.crm.catalog.repository.SubCategoryRepository;
import com.servicehub.crm.lead.dto.ServiceRequestResponse;
import com.servicehub.crm.lead.dto.ServiceRequestSaveRequest;
import com.servicehub.crm.lead.entity.Address;
import com.servicehub.crm.lead.entity.ServiceRequest;
import com.servicehub.crm.lead.enums.RequestStatus;
import com.servicehub.crm.lead.repository.AddressRepository;
import com.servicehub.crm.lead.repository.ServiceRequestRepository;
import com.servicehub.crm.lead.service.ServiceRequestService;
import com.servicehub.crm.mapper.ServiceRequestMapper;
import com.servicehub.crm.vendor.entity.Vendor;
import com.servicehub.crm.vendor.repository.VendorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final AddressRepository addressRepository;
    private final VendorRepository vendorRepository;
    private final ServiceRequestMapper serviceRequestMapper;

    public ServiceRequestServiceImpl(ServiceRequestRepository serviceRequestRepository, SubCategoryRepository subCategoryRepository, AddressRepository addressRepository, VendorRepository vendorRepository, ServiceRequestMapper serviceRequestMapper) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.addressRepository = addressRepository;
        this.vendorRepository = vendorRepository;
        this.serviceRequestMapper = serviceRequestMapper;
    }


    @Override
    @Transactional
    public ServiceRequestResponse save(ServiceRequestSaveRequest request) {
        Long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Required relation
        SubCategory subCategory = subCategoryRepository.findById(request.subCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-003", "Sub-category not found: " + request.subCategoryId()));

        // Optional relations — null-checked before even attempting a lookup
        Address address = request.addressId() != null
                ? addressRepository.findById(request.addressId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-004", "Address not found: " + request.addressId()))
                : null;

        Vendor vendor = request.assignedVendorId() != null
                ? vendorRepository.findById(request.assignedVendorId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-001", "Vendor not found: " + request.assignedVendorId()))
                : null;

        ServiceRequest serviceRequest;
        boolean isCreate = request.id() == null;

        if (isCreate) {
            serviceRequest = serviceRequestMapper.toEntity(request);
            serviceRequest.setCustomerId(currentUserId);
            serviceRequest.setRequestCode(CodeGeneratorUtil.generate("REQ"));
            serviceRequest.setStatus(RequestStatus.NEW);
        } else {
            serviceRequest = serviceRequestRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-005", "Lead not found: " + request.id()));
            serviceRequestMapper.updateEntityFromRequest(request, serviceRequest);
        }

        serviceRequest.setSubCategory(subCategory);
        serviceRequest.setAddress(address);

        if (vendor != null && serviceRequest.getAssignedVendor() == null) {
            serviceRequest.setStatus(RequestStatus.ASSIGNED);
        }
        serviceRequest.setAssignedVendor(vendor);

        ServiceRequest saved = serviceRequestRepository.save(serviceRequest);
        return serviceRequestMapper.toResponse(saved);
    }

}
