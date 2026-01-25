package com.bash.LytApp.dto;

import com.bash.LytApp.entity.BillStatus;

import java.math.BigDecimal;

public record BillUpdateResponseDto(
        String meterNumber,
        BigDecimal amount,
        BillStatus status
) {
}
