package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.BillStatus;
import com.bash.LytApp.repository.projection.BillView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    List<BillView> findByMeterNumber (String meterNumber);
    List<BillView> findByUserId(Long userId);
    List<BillView> findByUserIdAndStatus(Long userId, BillStatus status);
    List<BillView> findByStatus(BillStatus bill);
    // Optional: Optimized fetch to avoid N+1 issues if you need User data with the Bill
//    @Query("SELECT b FROM Bill b JOIN FETCH b.user WHERE b.user.id = :userId")
//    List<Bill> findAllByUserIdWithUser(@Param("userId") Long userId);
}
