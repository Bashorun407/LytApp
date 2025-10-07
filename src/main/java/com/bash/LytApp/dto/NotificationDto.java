package com.bash.LytApp.dto;

import com.bash.LytApp.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public record NotificationDto(

        User user,
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
