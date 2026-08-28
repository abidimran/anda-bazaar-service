package com.andabazaar.notification;

public interface PushNotificationService {

    void sendNotification( Long userId, String title, String message);
}