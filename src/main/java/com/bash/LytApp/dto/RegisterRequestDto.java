package com.bash.LytApp.dto;

public record RegisterRequestDto(
        String firstName,
        String lastName,
        String email,
        String password

) {
    public RegisterRequestDto{
        if(firstName == null || firstName.isBlank()){
            throw new IllegalArgumentException("Fist name cannot be empty");
        }

        if(lastName == null || lastName.isBlank()){
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        if(email == null || email.isBlank()){
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if(password == null || password.isBlank() || password.length() < 6){
            throw new IllegalArgumentException("Password name cannot be empty and cannot be less than 6 characters");
        }
    }
}
