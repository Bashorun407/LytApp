package com.bash.LytApp.service;

import com.bash.LytApp.dto.*;
import com.bash.LytApp.entity.PasswordResetToken;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
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
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    // Password reset token expiration time (1 hour)
    private static final int PASSWORD_RESET_TOKEN_EXPIRY_HOURS = 1;

    public AuthResponseDto login(LoginRequestDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // FIX: Cast the principal to UserDetails
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            // Get the full user entity for response
            User user = userRepository.findByEmail(loginRequest.email())
                    .orElseThrow(() -> new RuntimeException("User not found"));

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
        } catch (ClassCastException e) {
            throw new RuntimeException("Authentication error: " + e.getMessage());
        }
    }


    public AuthResponseDto register(RegisterRequestDto registerRequest) {

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new RuntimeException("Email already exists: " + registerRequest.email());
        }

        // Get or create USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("USER");
                    return roleRepository.save(newRole);
                });

        // Create new user
        User user = new User();
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());
        user.setEmail(registerRequest.email());
        user.setHashedPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(userRole);
        user.setEmailVerified(false);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        // Generate JWT token for auto-login after registration
        // FIX: Use UserDetailsService to load the user as UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String jwt = jwtUtil.generateToken(userDetails);

        //Send mail to notify user that they're logged in.
        emailService.sendVerificationEmail(registerRequest.email(), registerRequest.firstName(), jwt);

        return new AuthResponseDto(
                jwt,
                "Bearer",
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole().getName()
        );
    }


    //Method to call for forgot password
    public PasswordResetResponseDto forgotPassword(ForgotPasswordRequestDto request) {
        Optional<User> userOptional = userRepository.findByEmail(request.email());

        if (userOptional.isEmpty()) {
            // For security reasons, don't reveal if email exists or not
            return new PasswordResetResponseDto(
                    "If the email exists, a password reset link has been sent.",
                    true
            );
        }

        User user = userOptional.get();

        // Generate reset token
        String resetToken = generateResetToken();
        LocalDateTime tokenExpiry = LocalDateTime.now().plusHours(PASSWORD_RESET_TOKEN_EXPIRY_HOURS);

        // Save token and expiry to user (you might want to create separate table for reset tokens)
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(tokenExpiry);
        userRepository.save(user);

        // Send password reset email
        try {
            emailService.sendPasswordResetEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    resetToken
            );

            return new PasswordResetResponseDto(
                    "Password reset email sent successfully. Please check your inbox.",
                    true
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }
    //Method to call to reset password
    public PasswordResetResponseDto resetPassword(ResetPasswordRequestDto request) {
        // Find user by reset token
        Optional<User> userOptional = userRepository.findByResetToken(request.token());

        if (userOptional.isEmpty()) {
            return new PasswordResetResponseDto("Invalid or expired reset token.", false);
        }

        User user = userOptional.get();

        // Check if token is expired
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            // Clear the expired token
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);

            return new PasswordResetResponseDto("Reset token has expired. Please request a new one.", false);
        }

        // Update password
        user.setHashedPassword(passwordEncoder.encode(request.newPassword()));
        user.setResetToken(null); // Clear the used token
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return new PasswordResetResponseDto("Password reset successfully. You can now login with your new password.", true);
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
