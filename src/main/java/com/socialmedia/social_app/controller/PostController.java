package com.socialmedia.social_app.controller;

import com.socialmedia.social_app.dto.ApiResponse;
import com.socialmedia.social_app.dto.PostResponse;
import com.socialmedia.social_app.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Tag(name = "Post APIs", description = "Create & manage posts")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Create post",
            description = "Create a post with text content and optional image"
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        PostResponse post = postService.createPost(content, image);

        return ResponseEntity.ok(
                ApiResponse.success(post)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponse>>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Page<PostResponse> posts =
                postService.getAllPosts(page, size, sortBy, direction);

        return ResponseEntity.ok(
                ApiResponse.<Page<PostResponse>>builder()
                        .success(true)
                        .message("Posts fetched successfully")
                        .data(posts)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
