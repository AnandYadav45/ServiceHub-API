package com.servicehub.common.config;

import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

public class SecurityHeaderCustomizer {

    public static void apply(HeadersConfigurer<?> headers){
        headers
                .contentTypeOptions(c -> {})
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                .httpStrictTransportSecurity( hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000))
                .referrerPolicy( r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .cacheControl( c -> {});

    }
}
