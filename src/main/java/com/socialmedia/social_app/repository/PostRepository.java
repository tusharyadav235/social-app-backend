package com.socialmedia.social_app.repository;

import com.socialmedia.social_app.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findByUserId(Long userId);

    long countByUserId(Long userId);


    Page<Post> findAll(Pageable pageable);

    Page<Post> findByUserId(Long userId, Pageable pageable);
    void deleteByUserId(Long userId);




}
