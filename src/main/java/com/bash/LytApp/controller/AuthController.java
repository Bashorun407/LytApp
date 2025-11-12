package com.bash.LytApp.controller;

import com.bash.LytApp.dto.AuthRequestDto;
import com.bash.LytApp.dto.AuthResponseDto;
import com.bash.LytApp.dto.VerificationRequestDto;
import com.bash.LytApp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        AuthResponseDto authResponse = authService.authenticate(authRequestDto);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<AuthResponseDto> verify2FA(@Valid @RequestBody VerificationRequestDto verificationRequestDto) {
        AuthResponseDto authResponse = authService.verify2FA(verificationRequestDto);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationEmail(@RequestParam String email) {
        authService.sendVerificationEmail(email);
        return ResponseEntity.ok("Verification email sent successfully");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody VerificationRequestDto verificationRequestDto) {
        authService.verifyEmail(verificationRequestDto);
        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        authService.sendPasswordResetEmail(email);
        return ResponseEntity.ok("Password reset email sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }
}
