package com.bash.LytApp.service.ServiceImpl;

import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.exception.ResourceNotFoundException;
import com.bash.LytApp.mapper.UserMapper;
import com.bash.LytApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    //GET ALL USERS
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    //GET USER BY ID
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.mapToUserDto(user);
    }

    //CREATE USER
    public UserDto createUser(UserDto  userDto) {
        //check that User exists
        if (userRepository.existsByEmail(userDto.email()) == true)
            throw new ResourceNotFoundException("User already exists!!");

        User savedUser = userRepository.save(UserMapper.mapToCreateUser(userDto));
        return UserMapper.mapToUserDto(savedUser);
    }

    //UPDATE USER
    public UserDto updateUser(UserDto userDto){
        User user = userRepository.findByEmail(userDto.email())
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));

        User savedUser = userRepository.save(UserMapper.mapToUpdateUser(user, userDto));
        return UserMapper.mapToUserDto(savedUser);
    }

    //DELETE USER
    public String deleteUser(UserDto userDto){
        User user = userRepository.findByEmail(userDto.email())
                .orElseThrow(()-> new ResourceNotFoundException("User not found!"));

        userRepository.delete(user);

        return "User has been successfully deleted.";
    }


}
