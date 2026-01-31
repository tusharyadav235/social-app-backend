package com.socialmedia.social_app.controller;

import com.socialmedia.social_app.dto.ApiResponse;
import com.socialmedia.social_app.entity.Notification;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.repository.NotificationRepository;
import com.socialmedia.social_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(
            @AuthenticationPrincipal String email
    ) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );

        Long userId = user.getId();

        return ResponseEntity.ok(
                ApiResponse.success(
                        notificationRepository
                                .findByRecipientIdOrderByCreatedAtDesc(userId)
                )
        );
    }
}
