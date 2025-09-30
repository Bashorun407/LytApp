package com.bash.LytApp.service.ServiceImpl;

import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.entity.User;
import com.bash.LytApp.mapper.UserMapper;
import com.bash.LytApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

//    public List<UserDto> getAllUsers() {
//        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
//    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserDto(user);
    }


    public UserDto createUser(User user) {
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

//    private UserDto convertToDTO(User user) {
//        UserDto userDto = new UserDto();
//        userDto.setId(user.getId());
//        userDto.setFirstName(user.getFirstName());
//        userDto.setLastName(user.getLastName());
//        userDto.setEmail(user.getEmail());
//        userDto.setCreationDate(user.getCreationDate());
//        if (user.getRole() != null) {
//            userDto.setRoleName(user.getRole().getName());
//        }
//        return userDto;
//    }

}
