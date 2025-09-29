package com.bash.LytApp.dto;

import com.bash.LytApp.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

public record NotificationDto(
        Long id,
        User user,
        String type,
        String message,
        LocalDateTime sentAt,
        Boolean isRead

) {
}
