package com.bash.LytApp.repository.projection;

import com.bash.LytApp.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PaymentView {
    BigDecimal getAmountPaid();
    String getPaymentMethod();
    PaymentStatus getStatus();
    String getTransactionId();
    String getToken();
    LocalDateTime getPaidAt();
}
