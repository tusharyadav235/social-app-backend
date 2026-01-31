package com.socialmedia.social_app.repository;


import com.socialmedia.social_app.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    long countByFollowerId(Long userId);   // following count
    long countByFollowingId(Long userId); // followers count

    void deleteByFollowerId(Long userId);

    void deleteByFollowingId(Long userId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
