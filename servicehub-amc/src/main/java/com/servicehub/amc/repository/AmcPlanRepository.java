package com.servicehub.amc.repository;

import com.servicehub.amc.entity.AmcPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmcPlanRepository extends JpaRepository<AmcPlan, Long> {
}
