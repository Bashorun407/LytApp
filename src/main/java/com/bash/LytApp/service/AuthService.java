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
        //logger.info("Attempting registration for email: {}", registerRequest.getEmail());

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

        return new AuthResponseDto(
                jwt,
                "Bearer",
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
