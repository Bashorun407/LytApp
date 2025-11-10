package com.bash.LytApp.dto;

public record AuthResponseDto(
        String token,
        String type,
        String email,
        String firstName,
        String lastName,
        String role
) {
    public AuthResponseDto{
        type = "Bearer";
    }
}
