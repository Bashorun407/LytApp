package com.bash.LytApp.service;

import com.bash.LytApp.dto.*;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.email(),
                            loginRequestDto.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(getCurrentUser());

            User user = userRepository.findByEmail(loginRequestDto.email())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return new AuthResponseDto(
                    jwt,
                    "String",
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole().getName()
            );

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        // Check if email already exists
        if (userRepository.existsByEmail(registerRequestDto.email())) {
            throw new RuntimeException("Email already exists: " + registerRequestDto.email());
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
        user.setFirstName(registerRequestDto.firstName());
        user.setLastName(registerRequestDto.lastName());
        user.setEmail(registerRequestDto.email());
        user.setHashedPassword(passwordEncoder.encode(registerRequestDto.password()));
        user.setRole(userRole);
        user.setEmailVerified(false);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        // Generate JWT token for auto-login after registration
        String jwt = jwtUtil.generateToken(savedUser);

        return new AuthResponseDto(
                jwt,
                "String",
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getRole().getName()
        );
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
