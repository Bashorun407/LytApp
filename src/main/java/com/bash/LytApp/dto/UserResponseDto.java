package com.bash.LytApp.dto;

import java.time.LocalDateTime;

public record UserResponseDto(
        String firstName,
        String lastName,
        String email,
        String role,
        LocalDateTime modifiedDate
) {
}
