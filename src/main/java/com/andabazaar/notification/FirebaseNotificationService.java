package com.andabazaar.notification;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirebaseNotificationService
        implements PushNotificationService {

	//ye console mein notification log karega. 
	//Firebase credentials add karne ke baad actual mobile notification jayega.
	
    @Override
    public void sendNotification( Long userId, String title, String message) {

        /*
         * Firebase Cloud Messaging integration
         * will be added here.
         */

        log.info( "Push notification for user {}: {} - {}", userId, title, message);
    }
}