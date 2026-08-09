package com.servicehub.amc.service.Impl;

import com.servicehub.amc.dto.AmcSubscriptionResponse;
import com.servicehub.amc.dto.AmcSubscriptionSaveRequest;
import com.servicehub.amc.entity.AmcPlan;
import com.servicehub.amc.entity.AmcSubscription;
import com.servicehub.amc.enums.AmcStatus;
import com.servicehub.amc.mapper.AmcSubscriptionMapper;
import com.servicehub.amc.repository.AmcPlanRepository;
import com.servicehub.amc.repository.AmcSubscriptionRepository;
import com.servicehub.amc.service.AmcSubscriptionService;
import com.servicehub.common.config.CodeGeneratorUtil;
import com.servicehub.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

public class AmcSubscriptionServiceImpl implements AmcSubscriptionService {

    private final AmcSubscriptionRepository subscriptionRepository;
    private final AmcPlanRepository amcPlanRepository;
    private final AmcSubscriptionMapper subscriptionMapper;

    public AmcSubscriptionServiceImpl(AmcSubscriptionRepository subscriptionRepository, AmcPlanRepository amcPlanRepository, AmcSubscriptionMapper subscriptionMapper) {
        this.subscriptionRepository = subscriptionRepository;
        this.amcPlanRepository = amcPlanRepository;
        this.subscriptionMapper = subscriptionMapper;
    }


    @Override
    public AmcSubscriptionResponse save(AmcSubscriptionSaveRequest request) {
        Long currentUserId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AmcPlan plan = amcPlanRepository.findById(request.amcPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "AMC-404-002", "AMC plan not found: " + request.amcPlanId()));

        AmcSubscription subscription;

        if (request.id() == null) {
            subscription = new AmcSubscription();
            subscription.setCustomerId(currentUserId);
            subscription.setSubscriptionCode(CodeGeneratorUtil.generate("AMC"));
            subscription.setVisitsUsed(0);
            subscription.setStatus(AmcStatus.ACTIVE);
            subscription.setStartDate(LocalDate.now());
        } else {
            subscription = subscriptionRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "AMC-404-003", "Subscription not found: " + request.id()));
            // an id-provided call with a different amcPlanId reads as a plan change — allowed here,
            // recomputed below; no proration logic, deliberately kept simple
        }

        subscription.setAmcPlan(plan);
        subscription.setEndDate(subscription.getStartDate().plusMonths(plan.getDurationMonths()));

        AmcSubscription saved = subscriptionRepository.save(subscription);
        return subscriptionMapper.toResponse(saved);
    }
}
