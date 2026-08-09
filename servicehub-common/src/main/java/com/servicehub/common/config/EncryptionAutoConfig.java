package com.servicehub.common.config;

import com.servicehub.common.security.encryption.AesGcmEncryptor;
import com.servicehub.common.filter.EncryptionFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

@AutoConfiguration
@ConditionalOnProperty(prefix = "app.encryption", name = "enabled", havingValue = "false")
public class EncryptionAutoConfig {

    @Bean
    public AesGcmEncryptor aesGcmEncryptor(@Value("${app.encryption.key}") String base64Key) {
        return new AesGcmEncryptor(base64Key);
    }

    @Bean
    public FilterRegistrationBean<EncryptionFilter> encryptionFilterRegistration(AesGcmEncryptor encryptor) {
        FilterRegistrationBean<EncryptionFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new EncryptionFilter(encryptor));
        // Scoped deliberately — only the endpoints where it's actually worth the overhead.
        // Add paths here as you decide they need it; don't default to "/*".
        registration.addUrlPatterns("/api/v1/auth/login", "/api/v1/auth/otp/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);   // runs before Spring Security's whole chain
        return registration;
    }
}