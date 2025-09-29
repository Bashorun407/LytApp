package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByUserId(Long userId);
    List<Bill> findByUserIdAndStatus(Long userId, Bill.BillStatus status);
    List<Bill> findByStatus(Bill.BillStatus bill);
}
