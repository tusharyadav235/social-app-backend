package com.socialmedia.social_app.controller;

import com.socialmedia.social_app.dto.ApiResponse;
import com.socialmedia.social_app.dto.CommentResponse;
import com.socialmedia.social_app.entity.Comment;
import com.socialmedia.social_app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    @Autowired
    private  final CommentService commentService;

    @PostMapping("/{postId}")
    public CommentResponse addComment(
            @PathVariable Long postId,
            @RequestBody String text) {
        return commentService.addComment(postId,text);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<CommentResponse> comments =
                commentService.getCommentsByPost(postId, page, size);

        return ResponseEntity.ok(
                ApiResponse.<Page<CommentResponse>>builder()
                        .success(true)
                        .message("Comments fetched successfully")
                        .data(comments)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }



}
