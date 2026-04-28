package com.literature.russian_literature.users.domain.dto;

import com.literature.russian_literature.users.domain.UserRole;

public record UserResponse(
        Long id,
        String username,
        String email,
        UserRole role
) {

}
