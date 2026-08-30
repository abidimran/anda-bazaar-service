package com.andabazaar.repository;

import com.andabazaar.repository.entity.NotificationPreference;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationPreferenceRepository
        extends JpaRepository<
                NotificationPreference,
                Long> {
    Optional<NotificationPreference> findByUserId(Long userId);
}
