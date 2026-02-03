package com.bash.LytApp.dto.paystack;

public record PaystackInitializeResponseDto(
        String authorizationUrl,
        String reference
) {
}
