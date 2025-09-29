package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Bill;
import com.bash.LytApp.entity.Notification;
import com.bash.LytApp.entity.Payment;
import com.bash.LytApp.entity.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String hashedPassword,
        LocalDateTime creationDate,
        LocalDateTime modifiedDate,
        Role role,
        List<Bill> bills,
        List<Payment> payments,
        List<Notification> notifications
) {
}
