package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.PaymentResponseDto;
import com.bash.LytApp.entity.Payment;
import com.bash.LytApp.repository.projection.PaymentView;

public class PaymentMapper {

    //For Entity
    public static PaymentResponseDto mapToPaymentResponseDto(Payment payment){
        return new PaymentResponseDto(
                payment.getAmountPaid(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getToken(),
                payment.getPaidAt()
        );
    }

    //For Projection
    public static PaymentResponseDto mapToPaymentResponseDto(PaymentView paymentView){
        return new PaymentResponseDto(
                paymentView.getAmountPaid(),
                paymentView.getPaymentMethod(),
                paymentView.getStatus(),
                paymentView.getTransactionId(),
                paymentView.getToken(),
                paymentView.getPaidAt()
        );
    }
}
