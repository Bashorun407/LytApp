package com.bash.LytApp.mapper;

import com.bash.LytApp.dto.UserCreateDto;
import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserUpdateDto;
import com.bash.LytApp.entity.Role;
import com.bash.LytApp.entity.User;

import java.time.LocalDateTime;

public class UserMapper {

    public static User mapToCreateUser(User user, UserCreateDto userCreateDto){

        user.setFirstName(userCreateDto.firstName());
        user.setLastName(userCreateDto.lastName());
        user.setEmail(userCreateDto.email());
        user.setHashedPassword(userCreateDto.hashedPassword());
        user.setCreationDate(LocalDateTime.now());

        return user;
    }

    //Maps UserDto to Use
    public static User mapToUpdateUser(User user, UserUpdateDto userUpdateDto){

        user.setFirstName(userUpdateDto.firstName());
        user.setLastName(userUpdateDto.lastName());
        user.setHashedPassword(userUpdateDto.hashedPassword());
        user.setModifiedDate(LocalDateTime.now());

        return user;
    }
    //Maps User to UserDto
    public static UserDto mapToUserDto(User user){
        return new UserDto(
                //user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getHashedPassword(),
                user.getCreationDate(),
                user.getModifiedDate(),
                user.getRole().getName()
        );
    }
}
