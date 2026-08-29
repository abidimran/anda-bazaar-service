package com.andabazaar.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andabazaar.repository.entity.NotificationPreference;

public interface NotificationPreferenceRepository
        extends JpaRepository<
                NotificationPreference,
                Long> {

    Optional<NotificationPreference>
    findByUserId(Long userId);
}