package com.bash.LytApp.service;

import com.bash.LytApp.dto.PaymentRequestDto;
import com.bash.LytApp.dto.PaymentResponseDto;
import com.bash.LytApp.dto.paystack.PaystackInitializeResponseDto;

import java.util.List;

public interface PaymentService {
    // STEP 1: Initialize Payment (Returns Paystack URL)
    PaystackInitializeResponseDto initPayment(PaymentRequestDto paymentRequest);

    // STEP 2: Verify Payment (Finalizes transaction, updates Bill status & generates Token)
    PaymentResponseDto verifyAndCompletePayment(String reference);

    // Existing Read Operations
    PaymentResponseDto getPaymentById(Long id);
    List<PaymentResponseDto> getUserPayments(Long userId);
    List<PaymentResponseDto> getPaymentsByBillId(Long billId);
    PaymentResponseDto getPaymentByTransactionId(String transactionId);

}
