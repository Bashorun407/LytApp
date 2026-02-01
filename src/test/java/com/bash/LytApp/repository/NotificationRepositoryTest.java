package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Notification;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.repository.projection.NotificationView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NotificationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    private User testUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        // Create role
        userRole = new Role();
        userRole.setName("USER");
        entityManager.persistAndFlush(userRole);

        // Create user
        testUser = new User();
        testUser.setFirstName("Notification");
        testUser.setLastName("Test");
        testUser.setEmail("notification@example.com");
        testUser.setHashedPassword("hashedpass");
        testUser.setRole(userRole);
        entityManager.persistAndFlush(testUser);
    }

    @Test
    void findByUserIdOrderBySentAtDesc_ExistingUser_ReturnsOrderedNotifications() {
        // Given
        Notification notification1 = new Notification();
        notification1.setUser(testUser);
        notification1.setType("BILL_ALERT");
        notification1.setMessage("Bill due soon");
        notification1.setSentAt(LocalDateTime.now().minusHours(2));
        entityManager.persistAndFlush(notification1);

        Notification notification2 = new Notification();
        notification2.setUser(testUser);
        notification2.setType("PAYMENT_CONFIRMATION");
        notification2.setMessage("Payment received");
        notification2.setSentAt(LocalDateTime.now().minusHours(1));
        entityManager.persistAndFlush(notification2);

        // When
        List<NotificationView> notifications = notificationRepository
                .findByUserIdOrderBySentAtDesc(testUser.getId());

        // Then
        assertEquals(2, notifications.size());
        // Should be ordered by sentAt descending (newest first)
        assertTrue(notifications.get(0).getSentAt().isAfter(notifications.get(1).getSentAt()));
        assertEquals("PAYMENT_CONFIRMATION", notifications.get(0).getType());
    }

    @Test
    void findByUserIdAndIsReadFalse_UnreadNotifications_ReturnsOnlyUnread() {
        // Given
        Notification readNotification = new Notification();
        readNotification.setUser(testUser);
        readNotification.setType("BILL_ALERT");
        readNotification.setMessage("Read notification");
        readNotification.setIsRead(true);
        entityManager.persistAndFlush(readNotification);

        Notification unreadNotification = new Notification();
        unreadNotification.setUser(testUser);
        unreadNotification.setType("PAYMENT_CONFIRMATION");
        unreadNotification.setMessage("Unread notification");
        unreadNotification.setIsRead(false);
        entityManager.persistAndFlush(unreadNotification);

        // When
        List<NotificationView> unreadNotifications = notificationRepository
                .findByUserIdAndIsReadFalse(testUser.getId());

        // Then
        assertEquals(1, unreadNotifications.size());
        assertEquals("Unread notification", unreadNotifications.get(0).getMessage());
        assertFalse(unreadNotifications.get(0).getIsRead());
    }

    @Test
    void countByUserIdAndIsReadFalse_UnreadCount_ReturnsCorrectNumber() {
        // Given
        Notification notification1 = new Notification();
        notification1.setUser(testUser);
        notification1.setType("TYPE1");
        notification1.setMessage("Message 1");
        notification1.setIsRead(false);
        entityManager.persistAndFlush(notification1);

        Notification notification2 = new Notification();
        notification2.setUser(testUser);
        notification2.setType("TYPE2");
        notification2.setMessage("Message 2");
        notification2.setIsRead(false);
        entityManager.persistAndFlush(notification2);

        Notification readNotification = new Notification();
        readNotification.setUser(testUser);
        readNotification.setType("TYPE3");
        readNotification.setMessage("Message 3");
        readNotification.setIsRead(true);
        entityManager.persistAndFlush(readNotification);

        // When
        Long unreadCount = notificationRepository
                .countByUserIdAndIsReadFalse(testUser.getId());

        // Then
        assertEquals(2L, unreadCount);
    }

    @Test
    void saveNotification_ValidNotification_Success() {
        // Given
        Notification notification = new Notification();
        notification.setUser(testUser);
        notification.setType("TEST_TYPE");
        notification.setMessage("Test notification message");
        notification.setIsRead(false);

        // When
        Notification savedNotification = notificationRepository.save(notification);

        // Then
        assertNotNull(savedNotification.getId());
        assertEquals("TEST_TYPE", savedNotification.getType());
        assertEquals("Test notification message", savedNotification.getMessage());
        assertFalse(savedNotification.getIsRead());
        assertEquals(testUser.getId(), savedNotification.getUser().getId());
    }
}
