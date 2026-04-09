package com.literature.russian_literature.users.db;

import com.literature.russian_literature.users.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(), // Внимание: пароль в открытом виде!
                entity.getRole()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.username(),
                user.email(),
                user.password(), // Нужно будет шифровать перед сохранением!
                user.role()
        );
    }
}