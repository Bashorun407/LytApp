package com.bash.LytApp.dto;

import java.time.LocalDateTime;

public record UserUpdateDto(

        String firstName,
        String lastName,
        String email,
        String hashedPassword,
        String role
) {
    public UserUpdateDto {
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
