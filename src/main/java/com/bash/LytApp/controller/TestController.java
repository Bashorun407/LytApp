package com.bash.LytApp.controller;

import com.bash.LytApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/test")
public class TestController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send-test-email")
    public ResponseEntity<?> sendTestEmail(@RequestParam String email) {
        try {
            emailService.sendVerificationEmail("aprilweather@gmail.com", "Marvelous", "Test_Token: ********");
            return ResponseEntity.ok("Test email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send test email: " + e.getMessage());
        }
    }
}
