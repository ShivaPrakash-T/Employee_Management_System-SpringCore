package com.employee.notification;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("smsNotification")
public class SmsNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient, String message) {
        System.out.println("[SMS NOTIFICATION] Sending SMS to " + recipient + ": " + message);
    }
}
