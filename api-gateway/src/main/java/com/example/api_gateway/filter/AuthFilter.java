package com.example.api_gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;


@Component
@RefreshScope
public class AuthFilter implements GatewayFilter {

    @Value("${auth-service.base-url}")
    private String authServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        System.out.println("Request path: " + request.getPath());

        if (isAuthMissing(request)) {
            return onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
        }

        String token = getAuthHeader(request);

        if (validateTokenWithAuthService(token)) {
            System.out.println("Authentication is successful");
            return chain.filter(exchange);
        } else {
            return onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
        }
    }

    private boolean validateTokenWithAuthService(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(token, headers);

            ResponseEntity<Boolean> response = restTemplate.postForEntity(authServiceBaseUrl + "/auth/verify", entity, Boolean.class);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0).substring("Bearer ".length());
    }
}
