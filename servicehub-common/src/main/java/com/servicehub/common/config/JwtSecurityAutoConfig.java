package com.servicehub.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicehub.common.filter.JwtAuthenticationFilter;
import com.servicehub.common.security.jwt.JwtTokenProvider;
import com.servicehub.common.security.jwt.RestAccessDeniedHandler;
import com.servicehub.common.security.jwt.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration
@ConditionalOnClass(SecurityFilterChain.class)
public class JwtSecurityAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        return new JwtTokenProvider(secret, expirationMs);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationFilter jwtAuthFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        return new JwtAuthenticationFilter(jwtTokenProvider, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new RestAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestAccessDeniedHandler restAccessDeniedHandler(ObjectMapper objectMapper) {
        return new RestAccessDeniedHandler(objectMapper);
    }
}
