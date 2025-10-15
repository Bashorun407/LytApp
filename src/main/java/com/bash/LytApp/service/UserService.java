package com.bash.LytApp.service;

import com.bash.LytApp.dto.UserCreateDto;
import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(UserCreateDto userCreateDto);
    UserDto updateUser(Long id, UserUpdateDto userUpdateDto);
    void deleteUser(Long id);
    UserDto getUserByEmail(String email);
}
