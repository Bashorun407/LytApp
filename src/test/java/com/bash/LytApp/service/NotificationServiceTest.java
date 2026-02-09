package com.bash.LytApp.service;

import com.bash.LytApp.dto.NotificationDto;
import com.bash.LytApp.entity.Notification;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.repository.NotificationRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.repository.projection.NotificationView;
import com.bash.LytApp.service.ServiceImpl.NotificationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User testUser;
    private Notification testNotification;
    private NotificationDto testNotificationDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        testNotification = new Notification();
        testNotification.setId(1L);
        testNotification.setUser(testUser);
        testNotification.setType("PAYMENT_CONFIRMATION");
        testNotification.setMessage("Payment successful");
        testNotification.setSentAt(LocalDateTime.now());
        testNotification.setIsRead(false);

        testNotificationDto = new NotificationDto(
                "email", "PAYMENT_CONFIRMATION",
                LocalDateTime.now(), false
        );
    }

    @Test
    void getUserNotifications_WhenNotificationsExist_ReturnsNotificationList() {
        // Given
        NotificationView notificationView1 = mock(NotificationView.class);
        when(notificationView1.getType()).thenReturn("PAYMENT_CONFIRMATION");
        when(notificationView1.getMessage()).thenReturn("Payment successful");

        NotificationView notificationView2 = mock(NotificationView.class);
        when(notificationView2.getType()).thenReturn("BILL_ALERT");
        when(notificationView2.getMessage()).thenReturn("Bill due soon");

        when(notificationRepository.findByUserIdOrderBySentAtDesc(1L))
                .thenReturn(List.of(notificationView1, notificationView2));

        // When
        List<NotificationDto> notifications =
                notificationService.getUserNotifications(1L);

        // Then
        assertEquals(2, notifications.size());
        assertEquals("PAYMENT_CONFIRMATION", notifications.get(0).type());
        assertEquals("BILL_ALERT", notifications.get(1).type());

        verify(notificationRepository, times(1))
                .findByUserIdOrderBySentAtDesc(1L);
    }

    @Test
    void createNotification_WithValidData_ReturnsCreatedNotification() {
        // Given
        NotificationDto newNotificationDto = new NotificationDto(
                 "BILL_ALERT", "New bill issued", LocalDateTime.now(),  false
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification notification = invocation.getArgument(0);
            notification.setId(2L);
            return notification;
        });

        // When
        NotificationDto result = notificationService.createNotification(testUser.getId(), newNotificationDto);

        // Then
        assertNotNull(result);
        assertEquals("BILL_ALERT", result.type());
        assertEquals("New bill issued", result.message());
        verify(userRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void markNotificationAsRead_WhenNotificationExists_MarksAsRead() {
        // Given
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        // When
        notificationService.markNotificationAsRead(1L);

        // Then
        verify(notificationRepository, times(1)).findById(1L);
        verify(notificationRepository, times(1)).save(testNotification);
        assertTrue(testNotification.getIsRead());
    }

    @Test
    void getUnreadNotificationCount_ReturnsCorrectCount() {
        // Given
        when(notificationRepository.countByUserIdAndIsReadFalse(1L)).thenReturn(3L);

        // When
        Long count = notificationService.getUnreadNotificationCount(1L);

        // Then
        assertEquals(3L, count);
        verify(notificationRepository, times(1)).countByUserIdAndIsReadFalse(1L);
    }

    @Test
    void sendPaymentNotification_CreatesPaymentNotification() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        // When
        notificationService.sendPaymentNotification(1L, "Payment processed successfully");

        // Then
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void sendBillNotification_CreatesBillNotification() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        // When
        notificationService.sendBillNotification(1L, "New bill issued");

        // Then
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

}
