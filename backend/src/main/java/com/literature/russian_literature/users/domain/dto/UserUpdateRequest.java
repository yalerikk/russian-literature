package com.literature.russian_literature.users.domain.dto;

import com.literature.russian_literature.users.domain.UserRole;

public record UserUpdateRequest(
        String username,
        String email,
        String password,
        UserRole role
) {

}
