package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.NotificationDto;
import com.bash.LytApp.entity.Notification;
import com.bash.LytApp.repository.projection.NotificationView;

public class NotificationMapper {

    //For Entity
    public static NotificationDto mapToNotificationDto(Notification notification){
        return new NotificationDto(
                notification.getType(),
                notification.getMessage(),
                notification.getSentAt(),
                notification.getIsRead()
        );
    }

    //For interface-projection
    public static NotificationDto mapToNotificationDto(NotificationView notificationView){
        return new NotificationDto(
                notificationView.getType(),
                notificationView.getMessage(),
                notificationView.getSentAt(),
                notificationView.getIsRead()
        );
    }

    public static Notification mapToNotification(NotificationDto notificationDto){
        Notification notification = new Notification();
        notification.setType(notificationDto.type());
        notification.setMessage(notificationDto.message());
        notification.setSentAt(notificationDto.sentAt());
        notification.setIsRead(notificationDto.isRead());

        return notification;
    }
}
