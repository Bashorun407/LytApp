package com.bash.LytApp.controller;

import com.bash.LytApp.dto.*;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.mapper.UserMapper;
import com.bash.LytApp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "http://localhost:63342")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Register a new user")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        try {
            AuthResponseDto authResponse = authService.register(registerRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @Operation(summary = "Log in registered user")
    @ApiResponse(responseCode = "200", description = "Registered user logged in successfully")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            AuthResponseDto authResponse = authService.login(loginRequestDto);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @Operation(summary = "Provides forgot-password link")
    @ApiResponse(responseCode = "200", description = "Forgot-password link sent to user successfully")
    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto request) {
        try {
            PasswordResetResponseDto response = authService.forgotPassword(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new PasswordResetResponseDto(e.getMessage(), false));
        }
    }

    @Operation(summary = "Reset passwords for registered user")
    @ApiResponse(responseCode = "200", description = "Registered user's password reset successfully")
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) {
        try {
            PasswordResetResponseDto response = authService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new PasswordResetResponseDto(e.getMessage(), false));
        }
    }

    @Operation(summary = "Fetch current user's profile")
    @ApiResponse(responseCode = "200", description = "Current user's profile sent successfully")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserDto currentUserDto = authService.getCurrentUser();
        if (currentUserDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        return ResponseEntity.ok(currentUserDto);
    }
}
