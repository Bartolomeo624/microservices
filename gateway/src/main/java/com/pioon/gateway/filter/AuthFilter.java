package com.pioon.gateway.filter;

import com.pioon.gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate restTemplate;  // Ensure RestTemplate is correctly autowired and configured

    @Autowired
    private JwtUtil jwtUtil;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (validator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }

                String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header format");
                }

                try {
                    // REST call to AUTH service to validate the JWT token
                    ResponseEntity<Boolean> response = restTemplate.postForEntity("http://AUTH-SERVICE/auth/validate", authHeader, Boolean.class);
                    if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody())) {
                        String username = jwtUtil.extractUsername(authHeader);

                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("username", username)
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    } else {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
                    }
                } catch (Exception e) {
                    System.out.println("Access denied: " + e.getMessage());
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access to application", e);
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
