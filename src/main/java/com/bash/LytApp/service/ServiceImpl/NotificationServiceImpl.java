package com.bash.LytApp.service.ServiceImpl;

import com.bash.LytApp.dto.NotificationDto;
import com.bash.LytApp.entity.Notification;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.mapper.NotificationMapper;
import com.bash.LytApp.repository.NotificationRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<NotificationDto> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderBySentAtDesc(userId).stream()
                .map(NotificationMapper::mapToNotificationDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto createNotification(NotificationDto notificationDto) {
        User user = userRepository.findById(notificationDto.user().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + notificationDto.user().getId()));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(notificationDto.type());
        notification.setMessage(notificationDto.message());
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);

        Notification savedNotification = notificationRepository.save(notification);
        return NotificationMapper.mapToNotificationDto(savedNotification);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public Long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void sendPaymentNotification(Long userId, String message) {
        createNotification(new NotificationDto(
                null, "PAYMENT_CONFIRMATION", message, LocalDateTime.now(), false
        ));
    }

    @Override
    public void sendBillNotification(Long userId, String message) {
        createNotification(new NotificationDto(
                 null, "BILL_ALERT", message, LocalDateTime.now(), false
        ));
    }

}
