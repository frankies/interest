package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String traceId = UUID.randomUUID().toString();
        
        long startTime = System.currentTimeMillis();
        
        log.info("Request [TraceId: {}] - Method: {}, Path: {}, RemoteAddr: {}", 
            traceId,
            request.getMethod(),
            request.getPath(),
            request.getRemoteAddress()
        );

        // Add trace ID to request header
        ServerHttpRequest mutatedRequest = request.mutate()
            .header("X-Trace-Id", traceId)
            .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
            .doFinally(signalType -> {
                long duration = System.currentTimeMillis() - startTime;
                log.info("Response [TraceId: {}] - Status: {}, Duration: {}ms", 
                    traceId,
                    exchange.getResponse().getStatusCode(),
                    duration
                );
            });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
