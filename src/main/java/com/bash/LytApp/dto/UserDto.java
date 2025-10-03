package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Role;
import lombok.Getter;

import java.time.LocalDateTime;

public record UserDto(
        String firstName,
        String lastName,
        String email,
        LocalDateTime creationDate,
        LocalDateTime modifiedDate,
        Role role
) {
    public UserDto {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
    }
}
