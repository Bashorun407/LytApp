package com.bash.LytApp.security;

import com.bash.LytApp.entity.User;
import com.bash.LytApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getHashedPassword(),          // ✅ hashed password here
                List.of(user.getRole().getName())  // ✅ roles here
        );
    }
}
