package com.bash.LytApp.service;


import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);

}
