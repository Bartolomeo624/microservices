package com.pioon.gateway.filter;

import com.pioon.gateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (validator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = "Bearer " + authHeader.substring(7); // Ensure the full header is sent
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header format");
                }

                String finalAuthHeader = authHeader;
                return Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
                    try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.set(HttpHeaders.AUTHORIZATION, finalAuthHeader);
                        HttpEntity<String> entity = new HttpEntity<>(headers);

                        ResponseEntity<Boolean> response = restTemplate.exchange(
                                "http://AUTH-SERVICE/auth/validate",
                                HttpMethod.POST,
                                entity,
                                Boolean.class
                        );

                        if (response.getStatusCode() == HttpStatus.OK && Boolean.TRUE.equals(response.getBody())) {
                            String username = jwtUtil.extractUsername(finalAuthHeader.substring(7));

                            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                    .header("username", username)
                                    .build();
                            return chain.filter(exchange.mutate().request(modifiedRequest).build());
                        } else {
                            System.out.println("Authorization failed: " + response.getStatusCode() + " : " + response.getBody());
                            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
                        }
                    } catch (HttpClientErrorException e) {
                        System.out.println("Access denied: " + e.getRawStatusCode() + " : " + e.getResponseBodyAsString());
                        throw new ResponseStatusException(HttpStatus.valueOf(e.getRawStatusCode()), "Unauthorized access to application", e);
                    } catch (Exception e) {
                        System.out.println("Access denied: " + e.getMessage());
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access to application", e);
                    }
                }, executorService)).flatMap(identity -> identity);
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuration properties can be added here if needed
    }
}
