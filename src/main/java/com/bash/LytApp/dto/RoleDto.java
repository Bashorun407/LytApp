package com.bash.LytApp.dto;

import java.time.LocalDateTime;

public record RoleDto(
       Long id,
       String name,
       LocalDateTime created_at
) {
}
