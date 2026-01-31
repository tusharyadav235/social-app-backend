package com.socialmedia.social_app.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long userId;
    private String username;
    private String email;

    private long postCount;
    private long followerCount;
    private long followingCount;

    private boolean isFollowing;
    private List<PostResponse> posts;
}
