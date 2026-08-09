package com.servicehub.crm.config;

import com.servicehub.common.config.AbstractJpaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig extends AbstractJpaConfig {
    @Override protected String getBasePackage() {
        return "com.servicehub.crm";
    }
    @Override protected String getSchema() {
        return "crm";
    }
}
