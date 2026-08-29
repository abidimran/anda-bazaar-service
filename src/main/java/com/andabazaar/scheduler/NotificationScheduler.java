package com.andabazaar.scheduler;

import org.springframework.stereotype.Component;

import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
}
