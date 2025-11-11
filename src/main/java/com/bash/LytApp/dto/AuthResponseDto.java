package com.bash.LytApp.dto;

public record AuthResponseDto(
        String token,
        String type,
        String email,
        String message,
        boolean requires2FA
) {
    // Compact canonical constructor to set default value for 'type'
    public AuthResponseDto {
        if (type == null || type.isBlank()) {
            type = "Bearer";
        }
    }

    // Additional convenience constructors
    public AuthResponseDto (String token, String email) {
        this(token, "Bearer", email, null, false);
    }

    public AuthResponseDto (String message, boolean requires2FA) {
        this(null, "Bearer", null, message, requires2FA);
    }
}
