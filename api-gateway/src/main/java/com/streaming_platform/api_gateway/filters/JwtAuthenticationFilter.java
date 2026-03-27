package com.streaming_platform.api_gateway.filters;

import com.streaming_platform.api_gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Configuration
public class JwtAuthenticationFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.contains("/auth")) return chain.filter(exchange);

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return onError(exchange, "Missing Authorization header");
        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) return onError(exchange, "Invalid token");

        String userId = jwtUtil.extractUserId(token);

        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header("X-USER-ID", userId)
                .build();
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> onError(ServerWebExchange exchange, String error){
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(error.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
