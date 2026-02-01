package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Payment;
import com.bash.LytApp.repository.projection.PaymentView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<PaymentView> findByUserId(Long userId);
    List<PaymentView> findByBillId(Long billId);
    Optional<PaymentView> findByTransactionId(String transactionId);
}
