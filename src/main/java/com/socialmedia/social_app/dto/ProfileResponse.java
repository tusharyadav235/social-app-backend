package com.socialmedia.social_app.dto;
import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private Long userId;
    private String username;
    private String email;

    private long followers;
    private long following;

    private long totalPosts;
    private long totalLikes;

    private List<PostResponse> posts;

}
