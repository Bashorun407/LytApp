package com.bash.LytApp.dto;

import jakarta.validation.constraints.NotBlank;

public record VerificationRequestDto(
        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Token is required")
        String token
) {
}
