package com.servicehub.amc.service.Impl;

import com.servicehub.amc.dto.AmcPlanResponse;
import com.servicehub.amc.dto.AmcPlanSaveRequest;
import com.servicehub.amc.entity.AmcPlan;
import com.servicehub.amc.mapper.AmcPlanMapper;
import com.servicehub.amc.repository.AmcPlanRepository;
import com.servicehub.amc.service.AmcPlanService;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class AmcPlanServiceImpl implements AmcPlanService {

    private final AmcPlanRepository amcPlanRepository;
    private final AmcPlanMapper amcPlanMapper;

    public AmcPlanServiceImpl(AmcPlanRepository amcPlanRepository, AmcPlanMapper amcPlanMapper) {
        this.amcPlanRepository = amcPlanRepository;
        this.amcPlanMapper = amcPlanMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmcPlanResponse> findAll() {
        List<AmcPlanResponse> list = new ArrayList<>();
        for (AmcPlan amcPlan : amcPlanRepository.findAll()) {
            AmcPlanResponse response = AmcPlanMapper.toResponse(amcPlan);
            list.add(response);
        }
        return list;
    }

    @Override
    @Transactional
    public AmcPlanResponse save(AmcPlanSaveRequest request) {
        AmcPlan amcPlan;
        if (request.id() != null) {
            amcPlan = amcPlanRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "AMC-404-002", "AMC plan not found: " + request.id()));
            amcPlanMapper.updateEntityFromRequest(request, amcPlan);
        } else {
            amcPlan = amcPlanMapper.toEntity(request);
        }
        AmcPlan saved = amcPlanRepository.save(amcPlan);
        return AmcPlanMapper.toResponse(saved);
    }
}
