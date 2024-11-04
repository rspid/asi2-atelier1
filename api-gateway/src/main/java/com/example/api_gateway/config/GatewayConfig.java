package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.example.api_gateway.filter.AuthFilter;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthFilter authFilter;
    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r.path("/user/**")
                .and().method("GET").filters(f -> f.filters(authFilter))
                .uri("lb://user-service"))
            .route("card-service", r -> r.path("/card/**")
                .filters(f -> f.filters(authFilter))
                .uri("lb://card-service"))
            .route("sale-service", r -> r.path("/sale/**")
                .filters(f -> f.filters(authFilter))
                .uri("lb://sale-service"))
            .route("auth-service", r -> r.path("/auth/**")
                .uri("lb://auth-service"))
            .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
