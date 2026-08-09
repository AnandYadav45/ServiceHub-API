package com.servicehub.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(OpenAPI.class)
public class OpenApiAutoConfig {

    @Bean
    public OpenAPI serviceHubOpenAPI(){
        final String securitySchemaName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("ServiceHub API").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemaName))
                .components(new Components().addSecuritySchemes(securitySchemaName,
                        new SecurityScheme()
                                .name(securitySchemaName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));

    }
}
