package com.bash.LytApp.dto;

import com.bash.LytApp.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.util.List;

public record RoleDto(
       Long id,
       String name
       //List<User> users
) {
}
