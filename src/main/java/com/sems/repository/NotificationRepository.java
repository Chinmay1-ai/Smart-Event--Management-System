package com.sems.repository;

import com.sems.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);
    // generates: SELECT * FROM notifications WHERE user_id = ?
    // Used to show a user their notification history

    List<Notification> findBySentFalse();
    // generates: SELECT * FROM notifications WHERE sent = false
    // Used by a retry mechanism:
    // If email server was down, these notifications were created
    // but emails weren't sent — this finds them to retry
    // Spring Data reads "SentFalse" → WHERE sent = false
}