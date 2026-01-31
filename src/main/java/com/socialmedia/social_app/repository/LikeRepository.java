package com.socialmedia.social_app.repository;


import com.socialmedia.social_app.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    long countByPostId(Long postId);

    long countByPostUserId(Long userId);

    void deleteByUserId(Long userId);
}
