package com.bash.LytApp.service;

import com.bash.LytApp.dto.PaymentRequestDto;
import com.bash.LytApp.dto.PaymentResponseDto;

import java.util.List;

public interface PaymentService {
    PaymentResponseDto processPayment(PaymentRequestDto paymentRequest);
    PaymentResponseDto getPaymentById(Long id);
    List<PaymentResponseDto> getUserPayments(Long userId);
    List<PaymentResponseDto> getPaymentsByBillId(Long billId);
    PaymentResponseDto getPaymentByTransactionId(String transactionId);
}
