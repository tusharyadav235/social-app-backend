package com.socialmedia.social_app.controller;

import com.socialmedia.social_app.dto.AuthResponse;
import com.socialmedia.social_app.dto.LoginRequest;
import com.socialmedia.social_app.dto.RegisterRequest;
import com.socialmedia.social_app.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(name = "Auth APIs", description = "Authentication & Authorization APIs")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Operation(
            summary = "Login user",
            description = "Authenticate user and return JWT token"
    )
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(
            summary = "Register new user",
            description = "Create a new user account"
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}

