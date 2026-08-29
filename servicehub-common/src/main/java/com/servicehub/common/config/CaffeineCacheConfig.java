package com.servicehub.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import java.util.concurrent.TimeUnit;

@AutoConfiguration
@ConditionalOnClass(Caffeine.class)
@EnableCaching
public class CaffeineCacheConfig {


    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());

        // Categories change rarely — safe to cache for longer
        cacheManager.registerCustomCache("Categories",Caffeine.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(1,TimeUnit.HOURS)
                .recordStats()
                .build());

        // Register new cache here as we need.

        // Add this block inside the existing cacheManager() method, alongside the categories/vendorSummaries registrations:
        cacheManager.registerCustomCache("amcPlans", Caffeine.newBuilder()
                .maximumSize(50)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .recordStats()
                .build());

        return cacheManager;
    }

}
