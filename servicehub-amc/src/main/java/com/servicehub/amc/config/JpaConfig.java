package com.servicehub.amc.config;

import com.servicehub.common.config.AbstractJpaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig extends AbstractJpaConfig {
    @Override
    protected String getBasePackage() {
        return "com.servicehub.amc.entity";
    }

    @Override
    protected String getSchema() {
        return "amc";
    }
}
