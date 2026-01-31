package com.socialmedia.social_app.service;

import com.socialmedia.social_app.dto.LoginRequest;
import com.socialmedia.social_app.dto.RegisterRequest;
import com.socialmedia.social_app.entity.Role;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.exception.BadRequestException;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.repository.UserRepository;
import com.socialmedia.social_app.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public ResponseEntity<?> register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        Role role;

        // 👑 First user bootstrap logic
        if (!userRepository.existsByRole(Role.ADMIN)) {
            role = Role.ADMIN;
        } else {
            role = Role.USER;
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return null;
    }


    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with email: " + request.getEmail()
                        )
                );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}
