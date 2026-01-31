package com.socialmedia.social_app.service;

import com.socialmedia.social_app.entity.Notification;
import com.socialmedia.social_app.entity.NotificationType;
import com.socialmedia.social_app.entity.User;
import com.socialmedia.social_app.repository.NotificationRepository;
import com.socialmedia.social_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    public void sendNotification(
            Long recipientId,
            String message,
            NotificationType type
    ) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .type(type)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        // ✅ REAL-TIME PUSH
        messagingTemplate.convertAndSendToUser(
                recipientId.toString(),
                "/queue/notifications",
                notification
        );
    }
}
