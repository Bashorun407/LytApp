package com.bash.LytApp.dto;

import java.time.LocalDateTime;

public record UserCreateDto(
        String firstName,
        String lastName,
        String email,
        String hashedPassword,
        String role
) {
}
