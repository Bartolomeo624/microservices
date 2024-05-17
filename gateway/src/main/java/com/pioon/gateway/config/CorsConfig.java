//package com.pioon.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.server.ServerWebExchange;
//
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsWebFilter corsFilter() {
//        return new CorsWebFilter(exchange -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(Collections.singletonList("*"));
//            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//            config.setAllowedHeaders(Arrays.asList("*"));
//            config.setAllowCredentials(true);
//            return new CorsConfigurationSource() {
//                @Override
//                public CorsConfiguration getCorsConfiguration(jakarta.servlet.http.HttpServletRequest request) {
//                    return null;
//                }
//
//                @Override
//                public CorsConfiguration getCorsConfiguration(ServerWebExchange exchange) {
//                    return config;
//                }
//            };
//        });
//    }
//}