package com.socialmedia.social_app.service;

import com.socialmedia.social_app.dto.CommentResponse;
import com.socialmedia.social_app.entity.Comment;
import com.socialmedia.social_app.entity.NotificationType;
import com.socialmedia.social_app.entity.Post;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.repository.CommentRepository;
import com.socialmedia.social_app.repository.PostRepository;
import com.socialmedia.social_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public CommentResponse addComment(Long postId, String text) {

        User user = getLoggedInUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found with id: " + postId)
                );

        Comment comment = Comment.builder()
                .text(text)
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // ✅ Send notification only if not self-comment
        if (!user.getId().equals(post.getUser().getId())) {
            notificationService.sendNotification(
                    post.getUser().getId(),
                    user.getUsername() + " commented on your post",
                    NotificationType.COMMENT
            );
        }

        return mapToResponse(savedComment);
    }


    public Page<CommentResponse> getCommentsByPost(
            Long postId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return commentRepository.findByPostId(postId, pageable)
                .map(c -> CommentResponse.builder()
                        .id(c.getId())
                        .text(c.getText())
                        .postId(c.getPost().getId())
                        .userId(c.getUser().getId())
                        .username(c.getUser().getUsername())
                        .createdAt(c.getCreatedAt())
                        .build());
    }


    // 🔐 COMMON AUTH METHOD
    private User getLoggedInUser() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );
    }

    // 🔁 COMMON MAPPER
    private CommentResponse mapToResponse(Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
