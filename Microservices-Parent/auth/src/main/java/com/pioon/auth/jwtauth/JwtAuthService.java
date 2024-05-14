package com.pioon.auth.jwtauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAuthService implements AuthService{
    private final JwtService jwtService;

    @Override
    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}
