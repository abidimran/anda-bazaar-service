package com.andabazaar.scheduler;

import com.andabazaar.repository.NotificationRepository;
import com.andabazaar.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationScheduler Tests")
class NotificationSchedulerTest {
    @Mock private UserRepository userRepository;
    @Mock private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationScheduler scheduler;

    @Test
    @DisplayName("should instantiate scheduler")
    void shouldInstantiateScheduler() {
        assertThat(scheduler).isNotNull();
    }
}
