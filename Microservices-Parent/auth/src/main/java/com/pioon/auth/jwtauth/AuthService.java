package com.pioon.auth.jwtauth;

public interface AuthService {
    String generateToken(String username);
    boolean validateToken(String token);
}
