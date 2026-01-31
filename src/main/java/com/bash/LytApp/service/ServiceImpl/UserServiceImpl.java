package com.bash.LytApp.service.ServiceImpl;

import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserResponseDto;
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
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    //GET ALL USERS
    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserResponseDto).collect(Collectors.toList());
    }

    //GET USER BY ID
    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserMapper.mapToUserResponseDto(user);
    }

}
