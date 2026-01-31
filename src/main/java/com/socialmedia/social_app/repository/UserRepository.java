package com.socialmedia.social_app.repository;

import com.socialmedia.social_app.entity.Role;
import com.socialmedia.social_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByRole(Role role);


}
