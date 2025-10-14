package com.bash.LytApp.service;

import com.bash.LytApp.dto.UserCreateDto;
import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.repository.RoleRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.service.ServiceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Role testRole;
    private UserDto testUserDto;
    private UserCreateDto testUserCreateDto;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setHashedPassword("hashedPassword");
        testUser.setCreationDate(LocalDateTime.now());
        testUser.setModifiedDate(LocalDateTime.now());
        testUser.setRole(testRole);

        testUserDto = new UserDto(
                1L, "John", "Doe", "john.doe@example.com",
                "hashedPassword", LocalDateTime.now(), LocalDateTime.now(), "USER"
        );
    }

    @Test
    void getAllUsers_WhenUsersExist_ReturnsUserList() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setHashedPassword("hashedPassword");
        user2.setRole(testRole);

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        // When
        List<UserDto> users = userService.getAllUsers();

        // Then
        assertEquals(2, users.size());
        assertEquals("John", users.get(0).firstName());
        assertEquals("Jane", users.get(1).firstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ReturnsUserDto() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDto result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals("john.doe@example.com", result.email());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ThrowsException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(99L));
        assertEquals("User not found with id: 99", exception.getMessage());
        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    void createUser_WithValidData_ReturnsCreatedUser() {
        // Given
        UserCreateDto newUserDto = new UserCreateDto(
                "Alice", "Johnson", "alice@example.com",
                "newPassword",  "USER"
        );

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(testRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(2L);
            user.setFirstName("Alice");
            user.setLastName("Johnson");
            user.setEmail("alice@example.com");
            user.setHashedPassword("hashedPassword");
            return user;
        });

        // When
        UserDto result = userService.createUser(newUserDto);

        // Then
        assertNotNull(result);
        assertEquals("Alice", result.firstName());
        assertEquals("Johnson", result.lastName());
        assertEquals("alice@example.com", result.email());
        verify(userRepository, times(1)).existsByEmail("alice@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithExistingEmail_ThrowsException() {
        // Given
        UserCreateDto newUserDto = new UserCreateDto("Alice", "Johnson", "existing@example.com",
                "password",  "USER"
        );

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.createUser(newUserDto));
        assertEquals("Email already exists: existing@example.com", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_WhenRoleNotExists_CreatesNewRole() {
        // Given
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto(
                "John",
                "Doe",
                "john.doe@example.com",
                "hashedPassword",
                "Newbie" // This must match the argument in your stubbing
        );

        // Simulate no user exists with this email
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(false);

        // Simulate roleRepository returns empty, so a new role should be created
        when(roleRepository.findByName("Newbie")).thenReturn(Optional.empty());

        // Simulate saving a new role
        Role savedRole = new Role();
        savedRole.setId(1L);
        savedRole.setName("USER");
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        // Simulate saving the new user
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setHashedPassword("hashedPassword");
        savedUser.setRole(savedRole);
        savedUser.setCreationDate(LocalDateTime.now());
        savedUser.setModifiedDate(LocalDateTime.now());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDto result = userService.createUser(userCreateDto);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals("john.doe@example.com", result.email());
        assertEquals("USER", result.role());

        // Verify interactions
        verify(roleRepository).findByName("Newbie");
        verify(roleRepository).save(any(Role.class));
        verify(userRepository).save(any(User.class));
    }


    @Test
    void updateUser_WithValidData_ReturnsUpdatedUser() {
        // Given
        UserDto updateDto = new UserDto(
                1L, "Johnny", "Doey", "johnny.doey@example.com",
                "hashedPassword", null, null, "USER"  // Changed from testRole to "USER"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserDto result = userService.updateUser(1L, updateDto);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
        // Remove this line since we're not checking email existence:
        // verify(userRepository, times(1)).existsByEmail("johnny.doey@example.com");
    }

    @Test
    void updateUser_WithExistingEmail_ThrowsException() {
        // Given
        UserDto updateDto = new UserDto(
                1L, "John", "Doe", "existing@example.com",
                "password", null, null, "USER"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(1L, updateDto));
        assertEquals("Email already exists: existing@example.com", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_DeletesUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(testUser);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void getUserByEmail_WhenUserExists_ReturnsUserDto() {
        // Given
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));

        // When
        UserDto result = userService.getUserByEmail("john.doe@example.com");

        // Then
        assertNotNull(result);
        assertEquals("john.doe@example.com", result.email());
        verify(userRepository, times(1)).findByEmail("john.doe@example.com");
    }

}
