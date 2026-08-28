package com.andabazaar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.entity.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc( Long userId);

    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc( Long userId);

    long countByUserIdAndReadFalse( Long userId);

    long countByReadFalse();
}