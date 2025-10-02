package com.bash.LytApp.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserTest {

    @Autowired
    private EntityManager entityManager;
        @Test
        void userCreation_WithValidData_Success() {
            // Given
            Role role = new Role( "USER");

            // When
            User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setEmail("john.doe@example.com");
            user.setHashedPassword("hashedPassword123");
            user.setCreationDate(LocalDateTime.now());
            user.setModifiedDate(LocalDateTime.now());
            user.setRole(role);

            // Then
            assertNotNull(user);
            assertEquals("John", user.getFirstName());
            assertEquals("Doe", user.getLastName());
            assertEquals("john.doe@example.com", user.getEmail());
            assertEquals("hashedPassword123", user.getHashedPassword());
            assertEquals(role, user.getRole());
        }

    @Test
    void userCreation_WithNullFirstName_ThrowsException() {

        // Given
        User user = new User();
        user.setFirstName(null); // @Column(nullable = false)
        user.setLastName("Doe");
        user.setEmail("test@example.com");

        // When & Then
        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(user);
            entityManager.flush(); // This triggers DB constraints
        });
    }

    @Test
    void userEmail_UniqueConstraint_ThrowsException() {
            Role role = new Role("Test");
        entityManager.persist(role);

        // Given
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("unique@example.com");
        user1.setHashedPassword("hashedPassword123");
        user1.setCreationDate(LocalDateTime.now());
        user1.setModifiedDate(LocalDateTime.now());
        user1.setRole(role);
        // Set other required fields...

        User user2 = new User();
        user1.setFirstName("James");
        user1.setLastName("Buck");
        user1.setEmail("unique@example.com"); //same email
        user1.setHashedPassword("hashedPassword123");
        user1.setCreationDate(LocalDateTime.now());
        user1.setModifiedDate(LocalDateTime.now());
        user1.setRole(role);

        // When
        entityManager.persist(user1);
        entityManager.flush();

        // Then: persisting user2 with duplicate email should fail
        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(user2);
            entityManager.flush(); // Flush triggers DB constraint violation
        });
    }
}
