package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderBySentAtDesc(Long userId);
    List<Notification> findByUserIdAndIsReadFalse(Long userId);
    Long countByUserIdAndIsReadFalse(Long userId);
}
