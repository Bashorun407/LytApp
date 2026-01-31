package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByMeterNumber (String meterNumber);
    List<Bill> findByUserId(Long userId);
    List<Bill> findByUserIdAndStatus(Long userId, BillStatus status);
    List<Bill> findByStatus(BillStatus bill);
    // Optional: Optimized fetch to avoid N+1 issues if you need User data with the Bill
//    @Query("SELECT b FROM Bill b JOIN FETCH b.user WHERE b.user.id = :userId")
//    List<Bill> findAllByUserIdWithUser(@Param("userId") Long userId);
}
