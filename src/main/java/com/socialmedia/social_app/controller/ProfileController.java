package com.socialmedia.social_app.controller;


import com.socialmedia.social_app.dto.ApiResponse;
import com.socialmedia.social_app.dto.UserProfileResponse;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.repository.UserRepository;
import com.socialmedia.social_app.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService profileService;
    private final UserRepository userRepository;

    // 🔹 View ANY user's profile
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication authentication
    ) {
        String currentEmail = authentication.getName();
        Long currentUserId = getUserIdFromEmail(currentEmail);

        UserProfileResponse profile =
                profileService.getProfile(userId, currentUserId, page, size);

        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    // 🔹 View MY profile (logged-in user)
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> myProfile(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Long userId = getUserIdFromEmail(email);

        UserProfileResponse profile =
                profileService.getProfile(userId, userId, page, size);

        return ResponseEntity.ok(ApiResponse.success(profile));
    }

    private Long getUserIdFromEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();
    }
}




