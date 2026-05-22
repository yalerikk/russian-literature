package com.literature.russian_literature.users.util;

import com.literature.russian_literature.users.domain.dto.User;
import com.literature.russian_literature.users.domain.dto.UserUpdateRequest;
import com.literature.russian_literature.util.StringNormalizer;

import org.springframework.stereotype.Component;

@Component
public class UserNormalizer {
    private final StringNormalizer stringNormalizer;

    public UserNormalizer(StringNormalizer stringNormalizer) {
        this.stringNormalizer = stringNormalizer;
    }

    public User normalizeUser(User user) {
        return new User(
                user.id(),
                stringNormalizer.normalizeSpaces(user.username()),
                stringNormalizer.normalizeSpaces(user.email()),
                user.password(),
                user.role()
        );
    }

    public UserUpdateRequest normalizeUserUpdate(UserUpdateRequest request) {
        return new UserUpdateRequest(
                request.username() != null ? stringNormalizer.normalizeSpaces(request.username()) : null,
                request.email() != null ? stringNormalizer.normalizeSpaces(request.email()) : null,
                request.password() != null && !request.password().isBlank() ? request.password().trim() : null,
                request.role()
        );
    }
}
