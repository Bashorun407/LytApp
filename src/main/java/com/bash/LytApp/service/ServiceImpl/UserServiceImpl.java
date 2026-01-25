package com.bash.LytApp.service.ServiceImpl;

import com.bash.LytApp.dto.UserCreateDto;
import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserResponseDto;
import com.bash.LytApp.dto.UserUpdateDto;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.exception.ResourceNotFoundException;
import com.bash.LytApp.mapper.UserMapper;
import com.bash.LytApp.repository.RoleRepository;
import com.bash.LytApp.repository.UserRepository;
import com.bash.LytApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //GET ALL USERS
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserResponseDto).toList();
    }

    //GET USER BY ID
    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserMapper.mapToUserResponseDto(user);
    }

//    @Override
//    public UserResponseDto getUserByEmail(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
//        return UserMapper.mapToUserResponseDto(user);
//    }

    //CREATE USER
//    @Override
//    public UserResponseDto createUser(UserCreateDto userCreateDto) {
//
//        // Check if email already exists
//        if (userRepository.existsByEmail(userCreateDto.email())) {
//            throw new RuntimeException("Email already exists: " + userCreateDto.email());
//        }
//
//        // Get or create default role
//        Role userRole = roleRepository.findByName(userCreateDto.role())
//                .orElseGet(() -> {
//                    Role newRole = new Role();
//                    newRole.setName("USER");
//                    newRole.setCreatedAt(LocalDateTime.now());
//                    return roleRepository.save(newRole);
//                });
//
//        User user = User.builder().role(userRole).creationDate(LocalDateTime.now())
//                        .emailVerified(false).twoFactorEnabled(false).lastLogin(LocalDateTime.now()).build();
//
//        User savedUser = userRepository.save(UserMapper.mapToCreateUser(user, userCreateDto));
//        return UserMapper.mapToUserResponseDto(savedUser);
//    }

//    @Override
//    public UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto) {
//
//        User existingUser = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//
//        // Check if email is being changed and if it's already taken
//        if (!existingUser.getEmail().equals(userUpdateDto.email()) &&
//                userRepository.existsByEmail(userUpdateDto.email())) {
//            throw new RuntimeException("Email already exists: " + userUpdateDto.email());
//        }
//
//        //Setting new role
//        if(!userUpdateDto.role().isBlank()){
//            Role role = roleRepository.findByName(userUpdateDto.role())
//                            .orElseGet(()->{
//                                Role newRole = new Role(userUpdateDto.role());
//                                newRole.setCreatedAt(LocalDateTime.now());
//                                return roleRepository.save(newRole);
//                                    }
//                            );
//            existingUser.setRole(role);
//        }
//
//
//        User updatedUser = userRepository.save(UserMapper.mapToUpdateUser(existingUser, userUpdateDto));
//        return UserMapper.mapToUserResponseDto(updatedUser);
//    }

//    @Override
//    public void deleteUser(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//        userRepository.delete(user);
//    }

}
