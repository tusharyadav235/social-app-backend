package com.socialmedia.social_app.service;

import com.socialmedia.social_app.entity.Follow;
import com.socialmedia.social_app.entity.NotificationType;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.repository.FollowRepository;
import com.socialmedia.social_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public String followUser(Long targetUserId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User follower = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );

        if (follower.getId().equals(targetUserId)) {
            return "You cannot follow yourself";
        }

        User following = userRepository.findById(targetUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Target user not found with id: " + targetUserId)
                );

        if (followRepository
                .findByFollowerIdAndFollowingId(follower.getId(), following.getId())
                .isPresent()) {
            return "Already following this user";
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);

        // ✅ Always notify target user (self-follow already blocked)
        notificationService.sendNotification(
                following.getId(),
                follower.getUsername() + " started following you",
                NotificationType.FOLLOW
        );

        return "User followed successfully";
    }

    @Transactional
    public String unfollowUser(Long targetUserId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User follower = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );

        Follow follow = followRepository
                .findByFollowerIdAndFollowingId(follower.getId(), targetUserId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not following this user")
                );

        followRepository.delete(follow);
        return "User unfollowed successfully";
    }

    public long getFollowersCount(Long userId) {
        return followRepository.countByFollowingId(userId);
    }

    public long getFollowingCount(Long userId) {
        return followRepository.countByFollowerId(userId);
    }
}


