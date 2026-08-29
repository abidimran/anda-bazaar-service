package com.andabazaar.scheduler;

import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
}
