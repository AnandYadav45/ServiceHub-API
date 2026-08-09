package com.servicehub.amc.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public record AmcSubscriptionResponse (
    Long id,
    String subscriptionCode,
    Long customerId,
    Long amcPlanId,
    String planName,
    LocalDate startDate,
    LocalDate endDate,
    Integer visitsUsed,
    Integer includedVisits,
    String status

){ }
