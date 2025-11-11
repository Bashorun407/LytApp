package com.bash.LytApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank(message = "First name cannot be blank")
        String firstName,
        @NotBlank(message = "Last name cannot be blank")
        String lastName,
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String email,
        @NotBlank(message = "Password cannot be blank")
        String password

) {

}
