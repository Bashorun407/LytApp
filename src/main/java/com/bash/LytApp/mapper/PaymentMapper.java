package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.PaymentResponseDto;
import com.bash.LytApp.entity.Payment;

public class PaymentMapper {

    public static PaymentResponseDto mapToPaymentDto (Payment payment){
        return new PaymentResponseDto(
                payment.getBill(),
                payment.getUser(),
                payment.getAmountPaid(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getPaidAt()
        );
    }
}
