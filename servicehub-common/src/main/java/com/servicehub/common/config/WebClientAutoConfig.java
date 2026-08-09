package com.servicehub.common.config;

import com.servicehub.common.filter.AuthPropagationFilter;
import com.servicehub.common.filter.CorrelationIdPropagationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@AutoConfiguration
@ConditionalOnClass(WebClient.class)
public class WebClientAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .responseTimeout(Duration.ofSeconds(6))
                ))
                .filter(CorrelationIdPropagationFilter.propagate())
                .filter(AuthPropagationFilter.propagate());
    }
}
