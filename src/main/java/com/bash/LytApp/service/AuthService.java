package com.bash.LytApp.service;

import com.bash.LytApp.dto.*;
import com.bash.LytApp.entity.PasswordResetToken;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.mapper.UserMapper;
import com.bash.LytApp.repository.PasswordResetTokenRepository;
import com.bash.LytApp.repository.RoleRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.security.CustomUserDetailsService;
import com.bash.LytApp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    private static final int PASSWORD_RESET_TOKEN_EXPIRY_HOURS = 1;

    @Transactional
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        try {
            // 1. Authenticate credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 2. Fetch User Entity (Already verified by authenticationManager)
            User user = userRepository.findByEmail(loginRequest.email())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 3. Generate Token using our Optimized JwtUtil (embeds userId in claims)
            String jwt = jwtUtil.generateToken(user);

            return new AuthResponseDto(
                    jwt,
                    "Bearer",
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole().getName()
            );

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new RuntimeException("Email already exists: " + registerRequest.email());
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEmail(registerRequest.email());
        user.setHashedPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(userRole);
        user.setEmailVerified(false);
        user.setEnabled(true);

        // 4. Save and use the returned entity immediately
        User savedUser = userRepository.save(user);

        // 5. Optimized: Generate token without redundant DB hits
        String jwt = jwtUtil.generateToken(savedUser);

        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getFirstName(), jwt);

        return new AuthResponseDto(
                jwt,
                "Bearer",
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole().getName()
        );
    }

    public PasswordResetResponseDto forgotPassword(ForgotPasswordRequestDto request) {
        Optional<User> userOptional = userRepository.findByEmail(request.email());

        if (userOptional.isEmpty()) {
            return new PasswordResetResponseDto("If the email exists, a password reset link has been sent.", true);
        }

        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(PASSWORD_RESET_TOKEN_EXPIRY_HOURS));
        userRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), user.getFirstName(), resetToken);
            return new PasswordResetResponseDto("Password reset email sent successfully.", true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }

    @Transactional
    public PasswordResetResponseDto resetPassword(ResetPasswordRequestDto request) {
        User user = userRepository.findByResetToken(request.token())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token."));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
            throw new RuntimeException("Reset token has expired.");
        }

        user.setHashedPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return new PasswordResetResponseDto("Password reset successfully.", true);
    }

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return userRepository.findByEmail(authentication.getName())
                .map(UserMapper::mapToUserDto)
                .orElse(null);
    }
}
