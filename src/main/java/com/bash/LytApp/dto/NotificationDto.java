package com.bash.LytApp.dto;


import java.time.LocalDateTime;

public record NotificationDto(

        String type,
        String message,
        LocalDateTime sentAt,
        Boolean isRead

) {
    public NotificationDto {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Notification type cannot be null or empty");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Notification message cannot be null or empty");
        }
    }
}
