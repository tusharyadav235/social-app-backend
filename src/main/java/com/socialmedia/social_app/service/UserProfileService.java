package com.socialmedia.social_app.service;

import com.socialmedia.social_app.dto.PostResponse;
import com.socialmedia.social_app.dto.UserProfileResponse;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.mapper.PostMapper;
import com.socialmedia.social_app.repository.FollowRepository;
import com.socialmedia.social_app.repository.PostRepository;
import com.socialmedia.social_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    public UserProfileResponse getProfile(
            Long profileUserId,
            Long currentUserId,
            int page,
            int size
    ) {
        User user = userRepository.findById(profileUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<PostResponse> posts = postRepository
                .findByUserId(profileUserId, pageable)
                .map(PostMapper::toResponse);

        boolean isFollowing = false;

        // Only check follow status if viewing someone else's profile
        if (!profileUserId.equals(currentUserId)) {
            isFollowing = followRepository
                    .existsByFollowerIdAndFollowingId(currentUserId, profileUserId);
        }

        return UserProfileResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .postCount(postRepository.countByUserId(profileUserId))
                .followerCount(followRepository.countByFollowingId(profileUserId))
                .followingCount(followRepository.countByFollowerId(profileUserId))
                .isFollowing(isFollowing)
                .posts(posts.getContent())
                .build();
    }
}


