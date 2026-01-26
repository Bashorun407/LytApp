package com.bash.LytApp.dto;

public record AuthResponseDto(
        String token,
        String type,
        String email,
        String firstName,
        String lastName,
        String role
) {

    // Compact canonical constructor to set default value for 'type'
    public AuthResponseDto {
        if (type == null || type.isBlank()) {
            type = "Bearer";
        }
    }
}
