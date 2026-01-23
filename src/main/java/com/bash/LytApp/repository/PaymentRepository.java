package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    List<Payment> findByBillId(Long billId);
    Optional<Payment> findByTransactionId(String transactionId);
}
