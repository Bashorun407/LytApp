package com.bash.LytApp.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void notificationCreation_WithValidData_Success() {
        // Given
        User user = new User();
        user.setId(1L);

        // When
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType("PAYMENT_CONFIRMATION");
        notification.setMessage("Your payment was successful");
        notification.setSentAt(LocalDateTime.now());
        notification.setIsRead(false);

        // Then
        assertNotNull(notification);
        assertEquals(user, notification.getUser());
        assertEquals("PAYMENT_CONFIRMATION", notification.getType());
        assertEquals("Your payment was successful", notification.getMessage());
        assertFalse(notification.getIsRead());
    }

    @Test
    void notification_DefaultValues_Correct() {
        // When
        Notification notification = new Notification();

        // Then
        assertNotNull(notification.getSentAt());
        assertFalse(notification.getIsRead());
    }

    @Test
    void notificationMarkAsRead_UpdatesStatus() {
        // Given
        Notification notification = new Notification();

        // When
        notification.setIsRead(true);

        // Then
        assertTrue(notification.getIsRead());
    }
}
