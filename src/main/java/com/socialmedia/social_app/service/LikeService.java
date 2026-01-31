package com.socialmedia.social_app.service;

import com.socialmedia.social_app.entity.Like;
import com.socialmedia.social_app.entity.NotificationType;
import com.socialmedia.social_app.entity.Post;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.exception.ResourceNotFoundException;
import com.socialmedia.social_app.repository.LikeRepository;
import com.socialmedia.social_app.repository.PostRepository;
import com.socialmedia.social_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;


    @Transactional
    public String likePost(Long postId) {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Post not found with id: " + postId)
                );

        if (likeRepository.findByPostIdAndUserId(postId, user.getId()).isPresent()) {
            return "Post already liked";
        }

        Like like = Like.builder()
                .post(post)
                .user(user)
                .build();

        // ✅ Save first
        likeRepository.save(like);

        // ✅ Send notification only if not self-like
        if (!user.getId().equals(post.getUser().getId())) {
            notificationService.sendNotification(
                    post.getUser().getId(),
                    user.getUsername() + " liked your post",
                    NotificationType.LIKE
            );
        }

        return "Post liked successfully";
    }


    public String unlikePost(Long postId) {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with email: " + email)
                );

        Like like = likeRepository
                .findByPostIdAndUserId(postId, user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Like not found for this post")
                );

        likeRepository.delete(like);
        return "Post unliked successfully";
    }

    public long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
