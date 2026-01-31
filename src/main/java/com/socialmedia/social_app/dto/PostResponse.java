package com.socialmedia.social_app.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private Long id;
    private String content;
    private String imageUrl;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
}
