package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByMeterNumber(String meterNumber);
    Optional<Bill> findByEmail(String email);
    List<Bill> findByUserId(Long userId);
    List<Bill> findByEmailAndStatus(String email, Bill.BillStatus status);
    List<Bill> findByUserIdAndStatus(Long userId, Bill.BillStatus status);
    List<Bill> findByStatus(Bill.BillStatus bill);
}
