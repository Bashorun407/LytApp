package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.entity.User;

import java.time.LocalDateTime;

public class UserMapper {

    //Maps UserDto to Use
    public static User mapToUpdateUser(User user, UserDto userDto){

        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setModifiedDate(LocalDateTime.now());
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
