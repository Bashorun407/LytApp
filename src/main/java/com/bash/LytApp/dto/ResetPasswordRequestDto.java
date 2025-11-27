package com.bash.LytApp.dto;

public record ResetPasswordRequestDto(
        String token,
        String newPassword
) {
}
