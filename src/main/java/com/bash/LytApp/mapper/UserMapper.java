package com.bash.LytApp.mapper;

//import com.bash.LytApp.dto.UserCreateDto;
import com.bash.LytApp.dto.UserDto;
import com.bash.LytApp.dto.UserResponseDto;
//import com.bash.LytApp.dto.UserUpdateDto;
import com.bash.LytApp.entity.User;

public class UserMapper {

    public static UserResponseDto mapToUserResponseDto(User user){
        return new UserResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().toString(),
                user.getModifiedDate()
        );
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
                user.getRole().getName(),
                user.getEmailVerified(),
                user.getTwoFactorEnabled(),
                user.getLastLogin()
        );
    }
}
