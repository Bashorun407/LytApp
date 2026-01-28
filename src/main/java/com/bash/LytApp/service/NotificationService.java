package com.bash.LytApp.service;

import com.bash.LytApp.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getUserNotifications(Long userId);
    NotificationDto createNotification(Long userId, NotificationDto notificationDto);
    void markNotificationAsRead(Long notificationId);
    Long getUnreadNotificationCount(Long userId);
    void sendPaymentNotification(Long userId, String message);
    void sendBillNotification(Long userId, String message);
}
