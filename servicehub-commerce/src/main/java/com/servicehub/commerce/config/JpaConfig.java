package com.servicehub.commerce.config;

import com.servicehub.common.config.AbstractJpaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig extends AbstractJpaConfig {
    @Override
    protected String getBasePackage() {
        return "com.servicehub.commerce";
    }

    @Override
    protected String getSchema() {
        return "commerce";
    }
}
