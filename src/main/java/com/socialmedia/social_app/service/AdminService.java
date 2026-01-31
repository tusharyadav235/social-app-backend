package com.socialmedia.social_app.service;

import com.socialmedia.social_app.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void deleteUser(Long userId) {

        likeRepository.deleteByUserId(userId);
        commentRepository.deleteByUserId(userId);
        postRepository.deleteByUserId(userId);

        followRepository.deleteByFollowerId(userId);
        followRepository.deleteByFollowingId(userId);

        userRepository.deleteById(userId);
    }

}

