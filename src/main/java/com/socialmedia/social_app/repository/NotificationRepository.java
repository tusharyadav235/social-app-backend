package com.socialmedia.social_app.repository;

import com.socialmedia.social_app.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long userId);

    long countByRecipientIdAndReadFalse(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.createdAt < :date")
    void deleteOlderThan(@Param("date") LocalDateTime date);
}
