package com.bash.LytApp.dto;

public record LoginRequestDto(
        String email,
        String password
) {
    public LoginRequestDto{
        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if(password == null || password.isBlank()){
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
}
