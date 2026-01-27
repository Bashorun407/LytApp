package com.bash.LytApp.dto;

import java.time.LocalDateTime;

public record NotificationResponseDto(

        String type,
        String message,
        LocalDateTime sentAt,
        Boolean isRead
) {
}
