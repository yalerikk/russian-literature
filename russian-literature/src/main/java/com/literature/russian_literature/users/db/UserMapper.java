package com.literature.russian_literature.users.db;

import com.literature.russian_literature.users.domain.dto.User;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.username(),
                user.email(),
                user.password(),
                user.role()
        );
    }
}
