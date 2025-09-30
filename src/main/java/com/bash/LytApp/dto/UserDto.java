package com.bash.LytApp.dto;

import com.bash.LytApp.entity.Role;
import lombok.Getter;

import java.time.LocalDateTime;

public record UserDto(
        String firstName,
        String lastName,
        String email,
        LocalDateTime creationDate,
        LocalDateTime modifiedDate,
        Role role
) {
}
