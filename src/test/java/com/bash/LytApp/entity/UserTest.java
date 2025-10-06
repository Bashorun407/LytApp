package com.bash.LytApp.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void userCreation_WithValidData_Success() {
        Role role = new Role("USER");
        entityManager.persist(role);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setHashedPassword("hashedPassword123");
        user.setCreationDate(LocalDateTime.now());
        user.setModifiedDate(LocalDateTime.now());
        user.setRole(role);

        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("hashedPassword123", user.getHashedPassword());
        assertEquals(role, user.getRole());
    }

    @Test
    void userCreation_WithNullFirstName_ThrowsException() {
        Role role = new Role("USER");
        entityManager.persist(role);

        User user = new User();
        user.setFirstName(null); // should fail
        user.setLastName("Doe");
        user.setEmail("test@example.com");
        user.setHashedPassword("pass");
        user.setRole(role);

        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persist(user);
            entityManager.flush(); // triggers validation
        });
    }

    @Test
    void userEmail_UniqueConstraint_ThrowsException() {
        Role role = new Role("USER");
        entityManager.persist(role);

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("unique@example.com");
        user1.setHashedPassword("hashedPassword123");
        user1.setCreationDate(LocalDateTime.now());
        user1.setModifiedDate(LocalDateTime.now());
        user1.setRole(role);

        User user2 = new User();
        user2.setFirstName("James");
        user2.setLastName("Buck");
        user2.setEmail("unique@example.com"); // same email
        user2.setHashedPassword("hashedPassword123");
        user2.setCreationDate(LocalDateTime.now());
        user2.setModifiedDate(LocalDateTime.now());
        user2.setRole(role);

        entityManager.persist(user1);
        entityManager.flush();

        assertThrows(PersistenceException.class, () -> {
            entityManager.persist(user2);
            entityManager.flush();
        });
    }

    @Test
    void userValidation_WhenFirstNameIsBlank_ShouldFail() {
        User user = new User();
        user.setFirstName(""); // Invalid
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setHashedPassword("securePassword");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }
}
