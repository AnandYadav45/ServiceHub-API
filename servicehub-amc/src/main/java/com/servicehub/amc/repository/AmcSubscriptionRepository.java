package com.servicehub.amc.repository;


import com.servicehub.amc.entity.AmcSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmcSubscriptionRepository extends JpaRepository<AmcSubscription, Long> {
}
