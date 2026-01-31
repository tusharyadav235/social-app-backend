package com.socialmedia.social_app.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

    private Long id;
    private String text;
    private Long postId;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
}

