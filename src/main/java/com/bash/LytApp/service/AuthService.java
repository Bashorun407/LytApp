package com.bash.LytApp.service;

import com.bash.LytApp.dto.AuthRequestDto;
import com.bash.LytApp.dto.AuthResponseDto;
import com.bash.LytApp.dto.VerificationRequestDto;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.security.CustomUserDetailsService;
import com.bash.LytApp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    // In-memory storage for 2FA tokens (use Redis in production)
    private Map<String, String> twoFATokens = new HashMap<>();
    private Map<String, String> verificationTokens = new HashMap<>();

    public AuthResponseDto authenticate(AuthRequestDto authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.email())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.email());

        // Check if user has 2FA enabled
        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTwoFactorEnabled()) {
            String twoFAToken = generateRandomToken();
            twoFATokens.put(authRequest.email(), twoFAToken);

            // Send 2FA token via email
            emailService.sendVerificationEmail(user.getEmail(),
                    user.getFirstName() + " " + user.getLastName(), twoFAToken);

            return new AuthResponseDto("2FA token sent to your email", true);
        }

        final String jwt = jwtUtil.generateToken(userDetails);
        return new AuthResponseDto(jwt, authRequest.email());
    }


    public AuthResponseDto verify2FA(VerificationRequestDto verificationRequest) {
        String storedToken = twoFATokens.get(verificationRequest.email());

        if (storedToken == null || !storedToken.equals(verificationRequest.token())) {
            throw new RuntimeException("Invalid or expired 2FA token");
        }

        // Remove used token
        twoFATokens.remove(verificationRequest.email());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(verificationRequest.email());
        final String jwt = jwtUtil.generateToken(userDetails);

        return new AuthResponseDto(jwt, verificationRequest.email());
    }

    public void sendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String verificationToken = generateRandomToken();
        verificationTokens.put(email, verificationToken);

        emailService.sendVerificationEmail(email,
                user.getFirstName() + " " + user.getLastName(), verificationToken);
    }

    public void verifyEmail(VerificationRequestDto verificationRequest) {
        String storedToken = verificationTokens.get(verificationRequest.email());

        if (storedToken == null || !storedToken.equals(verificationRequest.token())) {
            throw new RuntimeException("Invalid or expired verification token");
        }

        User user = userRepository.findByEmail(verificationRequest.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        user.setModifiedDate(LocalDateTime.now());
        userRepository.save(user);

        // Remove used token
        verificationTokens.remove(verificationRequest.email());
    }

    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = jwtUtil.generateVerificationToken(email);

        // In production, store this token in database with expiration
        verificationTokens.put(email + "_reset", resetToken);

        emailService.sendPasswordResetEmail(email,
                user.getFirstName() + " " + user.getLastName(), resetToken);
    }

    public void resetPassword(String token, String newPassword) {
        try {
            String email = jwtUtil.validateVerificationToken(token);
            String storedToken = verificationTokens.get(email + "_reset");

            if (storedToken == null || !storedToken.equals(token)) {
                throw new RuntimeException("Invalid or expired reset token");
            }

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            user.setHashedPassword(passwordEncoder.encode(newPassword));
            user.setModifiedDate(LocalDateTime.now());
            userRepository.save(user);

            // Remove used token
            verificationTokens.remove(email + "_reset");

        } catch (Exception e) {
            throw new RuntimeException("Invalid or expired reset token");
        }
    }

    private String generateRandomToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000); // 6-digit token
        return String.valueOf(token);
    }
}
