package com.socialmedia.social_app.scheduler;


import com.socialmedia.social_app.repository.NotificationRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@EnableScheduling
@Component
public class NotificationCleanupJob {

    private final NotificationRepository notificationRepository;

    public NotificationCleanupJob(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanOldNotifications() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteOlderThan(cutoff);
        System.out.println("🧹 Old notifications cleaned");
    }
}
