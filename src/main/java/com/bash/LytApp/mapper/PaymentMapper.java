package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.PaymentDto;
import com.bash.LytApp.entity.Payment;

public class PaymentMapper {

    public static PaymentDto mapToPaymentDto (Payment payment){
        return new PaymentDto(
                payment.getBill(),
                payment.getUser(),
                payment.getAmountPaid(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getPaidAt()
        );
    }

    public static Payment mapToPayment(PaymentDto paymentDto){
        Payment payment = new Payment();
        payment.setBill(paymentDto.bill());
        payment.setUser(paymentDto.user());
        payment.setAmountPaid(paymentDto.amountPaid());
        payment.setPaymentMethod(paymentDto.paymentMethod());
        payment.setStatus(paymentDto.status());
        payment.setTransactionId(paymentDto.transactionId());
        payment.setPaidAt(paymentDto.paidAt());

        return payment;
    }
}
