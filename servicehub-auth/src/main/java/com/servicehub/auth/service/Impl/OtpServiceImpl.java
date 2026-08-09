package com.servicehub.auth.service.Impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.servicehub.auth.service.OtpService;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class OtpServiceImpl implements OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();

    // In-memory, per-instance — Caffeine's own expiry handles the 5-minute TTL directly
    private final Cache<String, String> otpStore = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();


    @Override
    public String generateAndStore(String phoneNumber) {
        String otp = String.format("%906d", RANDOM.nextInt(1000000));
        otpStore.put(phoneNumber, otp);

        return otp;
    }

    @Override
    public boolean verify(String phoneNumber, String otp) {
        String stored = otpStore.getIfPresent(phoneNumber);
        boolean valid = stored != null && stored.matches(otp);
        if(valid){
            otpStore.invalidate(phoneNumber); // Single-use
        }
        return valid;
    }
}
