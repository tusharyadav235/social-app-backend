package com.socialmedia.social_app.mapper;


import com.socialmedia.social_app.dto.PostResponse;
import com.socialmedia.social_app.entity.Post;

public class PostMapper {

    public static PostResponse toResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
