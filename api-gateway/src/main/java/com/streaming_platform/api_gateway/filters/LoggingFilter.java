package com.streaming_platform.api_gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Logging filter for tracking incoming requests and outgoing responses.
 *
 * <p>This filter:
 * <ul>
 *     <li>Logs request details</li>
 *     <li>Adds correlation ID for tracing</li>
 *     <li>Logs response completion</li>
 * </ul>
 */
@Slf4j
@Configuration
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String correlationId = UUID.randomUUID().toString();

        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();

        long startTime = System.currentTimeMillis();

        log.info("[{}] Incoming Request: {} {}", correlationId, method, path);

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("[{}] Response Sent | Duration: {} ms", correlationId, duration);
                })
                .doOnError(error -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.error("[{}] Error occurred | Duration: {} ms | Error: {}",
                            correlationId, duration, error.getMessage());
                });
    }

    @Override
    public int getOrder() {
        return -2; // Runs BEFORE auth filter
    }
}