package com.employee.notification;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[EMAIL NOTIFICATION] Sending email to " + recipient + ": " + message);
    }
}
