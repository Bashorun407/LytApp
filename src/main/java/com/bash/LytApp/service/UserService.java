package com.bash.LytApp.service;

import com.bash.LytApp.dto.UserCreateDto;
import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserResponseDto;
import com.bash.LytApp.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    //UserResponseDto createUser(UserCreateDto userCreateDto);
    //UserResponseDto updateUser(Long id, UserUpdateDto userUpdateDto);
    //void deleteUser(Long id);
    //UserResponseDto getUserByEmail(String email);
}
