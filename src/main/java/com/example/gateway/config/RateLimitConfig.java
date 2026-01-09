package com.example.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest()
                .getRemoteAddress()
                .getAddress()
                .getHostAddress()
        );
    }

    @Bean
    @Primary
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(
            exchange.getRequest()
                .getHeaders()
                .getFirst("X-User-Id") != null 
                    ? exchange.getRequest().getHeaders().getFirst("X-User-Id")
                    : "anonymous"
        );
    }
}
