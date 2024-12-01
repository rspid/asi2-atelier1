package com.example.api_gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class GatewayConfig {

    
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("backend-service", r -> r.path("/api/spring/**")
                .filters(f -> f.stripPrefix(2))  // Retire "api/spring" de l'URL
                .uri("lb://backend-service"))

            .route("request-management-service", r -> r.path("/api/requests/**")
                .filters(f -> f.stripPrefix(2))  
                .uri("lb://request-management-service"))
                
            // Route pour le backend Node.js
            // .route("node-backend", r -> r.path("/api/node/**")
            //     .filters(f -> f.stripPrefix(2))  // Retire "api/node" de l'URL
            //     .uri("http://localhost:3000"))
            .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
