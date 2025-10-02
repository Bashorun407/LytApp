package com.bash.LytApp.repository;

import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_ExistingEmail_ReturnsUser() {
        // Given
        Role role = new Role();
        role.setName("USER");
        entityManager.persistAndFlush(role);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setHashedPassword("hashed123");
        user.setCreationDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setRole(role);
        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByEmail("john.doe@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Doe", found.get().getLastName());
    }

    @Test
    void findByEmail_NonExistingEmail_ReturnsEmpty() {
        // When
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        // Given
        Role role = new Role();
        role.setName("USER");
        entityManager.persistAndFlush(role);

        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        user.setHashedPassword("hashed456");
        user.setCreationDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setRole(role);
        entityManager.persistAndFlush(user);

        // When
        boolean exists = userRepository.existsByEmail("jane.smith@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void saveUser_ValidUser_Success() {
        // Given
        Role role = new Role();
        role.setName("ADMIN");
        entityManager.persistAndFlush(role);

        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Johnson");
        user.setEmail("alice.johnson@example.com");
        user.setHashedPassword("hashed789");
        user.setCreationDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setRole(role);

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("Alice", savedUser.getFirstName());
        assertEquals("alice.johnson@example.com", savedUser.getEmail());
    }

    @Test
    void findAllUsers_WithMultipleUsers_ReturnsAll() {
        // Given
        Role role = new Role();
        role.setName("USER");
        entityManager.persistAndFlush(role);

        User user1 = new User();
        user1.setFirstName("User1");
        user1.setLastName("Test");
        user1.setEmail("user1@example.com");
        user1.setHashedPassword("pass1");
        user1.setRole(role);
        entityManager.persistAndFlush(user1);

        User user2 = new User();
        user2.setFirstName("User2");
        user2.setLastName("Test");
        user2.setEmail("user2@example.com");
        user2.setHashedPassword("pass2");
        user2.setRole(role);
        entityManager.persistAndFlush(user2);

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user1@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("user2@example.com")));
    }
}
