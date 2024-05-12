package com.pioon.auth.jwtauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class JwtAuthController {
    private final AuthService authService;
    private final AuthenticationManager authManager;

    @Autowired
    public JwtAuthController(AuthService authService, AuthenticationManager authManager) {
        this.authService = authService;
        this.authManager = authManager;
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );
        if (authenticate.isAuthenticated()) {
            return authService.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestParam("token") String token) {
        return authService.validateToken(token);
    }
}
