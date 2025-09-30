package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.entity.User;

import java.time.LocalDateTime;

public class UserMapper {
    //Maps UserDto to User
    public static User mapToUser(UserDto userDto){
        User user = new User();

        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEmail(userDto.email());
        user.setCreationDate(userDto.creationDate());
        user.setModifiedDate(userDto.modifiedDate());
        user.setRole(userDto.role());

        return user;
    }
    //Maps User to UserDto
    public static UserDto mapToUserDto(User user){
        return new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreationDate(),
                user.getModifiedDate(),
                user.getRole()
        );
    }

}
