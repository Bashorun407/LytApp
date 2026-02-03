package com.bash.LytApp.dto.paystack;

public record PaystackVerificationResponseDto(
        boolean success,
        String status,
        String gatewayResponse
) {
}
