package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.NotificationDto;
import com.bash.LytApp.entity.Notification;

import java.time.LocalDateTime;

public class NotificationMapper {

    public static NotificationDto mapToNotificationDto(Notification notification){
        return new NotificationDto(
                notification.getUser(),
                notification.getType(),
                notification.getMessage(),
                notification.getSentAt(),
                notification.getIsRead()
        );
    }

    public static Notification mapToNotification(NotificationDto notificationDto){
        Notification notification = new Notification();

        notification.setUser(notificationDto.user());
        notification.setType(notificationDto.type());
        notification.setMessage(notificationDto.message());
        notification.setSentAt(notificationDto.sentAt());
        notification.setIsRead(notificationDto.isRead());

        return notification;
    }
}
