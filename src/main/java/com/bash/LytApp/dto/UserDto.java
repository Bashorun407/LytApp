package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Role;
import lombok.Getter;

import java.time.LocalDateTime;

public record UserDto(
        //Long id,
        String firstName,
        String lastName,
        String email,
        String hashedPassword,
        LocalDateTime creationDate,
        LocalDateTime modifiedDate,
        //Role role
        String role
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
        if (hashedPassword == null || hashedPassword.isBlank()){
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }
}
