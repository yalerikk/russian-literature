package com.literature.russian_literature.users.domain;

import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.users.db.UserMapper;
import com.literature.russian_literature.users.db.UserRepository;

import com.literature.russian_literature.users.util.UserNormalizer;
import com.literature.russian_literature.users.util.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserNormalizer userNormalizer;
    private final UserValidator userValidator;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, UserNormalizer userNormalizer,
                       UserValidator userValidator
    ) {
        this.repository = userRepository;
        this.mapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userNormalizer = userNormalizer;
        this.userValidator = userValidator;
    }

    public User getUserById (
            Long id
    ) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Пользователь с id = " + id + " не найден"
                ));

        return mapper.toDomain(userEntity);
    }

    public List<User> getAllUsers() {
        List<UserEntity> allUsers = repository.findAll();

        return allUsers.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Transactional
    public User createUser(
            User userToCreate
    ) {
        // Нормализация перед проверками
        User normalizedUser = userNormalizer.normalizeUser(userToCreate);
        userValidator.validateForCreate(normalizedUser);

        var entityToSave = mapper.toEntity(normalizedUser);
        entityToSave.setRole(UserRole.READER); // Все новые пользователи - читатели
        entityToSave.setPassword(passwordEncoder.encode(entityToSave.getPassword()));

        var savedEntity = repository.save(entityToSave);
        log.info("Создан пользователь: '{}' с id = {}", savedEntity.getUsername(), savedEntity.getId());

        return mapper.toDomain(savedEntity);
    }

    public User loginUser(
            LoginRequest loginRequest
    ) {
        // Пытаемся найти по username, затем по email
        UserEntity userEntity = repository.findByUsername(loginRequest.login())
                .orElseGet(() -> repository.findByEmail(loginRequest.login())
                        .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));

        // Проверяем пароль
        if (!passwordEncoder.matches(loginRequest.password(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Неверный пароль");
        }

        log.info("Авторизовался пользователь: '{}' с id = {}", userEntity.getUsername(), userEntity.getId());
        return mapper.toDomain(userEntity);
    }

    @Transactional
    public User updateUser(
            Long id,
            User userUpdate,
            boolean isAdmin
    ) {
        UserEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        // Нормализуем данные перед валидацией
        User normalizedUpdate = userNormalizer.normalizeUser(userUpdate);

        // Валидация данных
        userValidator.validateForUpdate(id, normalizedUpdate);

        // Проверяем уникальность username, если он меняется
        if (normalizedUpdate.username() != null && !normalizedUpdate.username().equals(existing.getUsername())) {
            existing.setUsername(normalizedUpdate.username());
        }

        // Проверяем уникальность email, если он меняется
        if (normalizedUpdate.email() != null && !normalizedUpdate.email().equals(existing.getEmail())) {
            existing.setEmail(normalizedUpdate.email());
        }

        // Обновляем пароль, если он предоставлен
        if (normalizedUpdate.password() != null) {
            existing.setPassword(passwordEncoder.encode(normalizedUpdate.password()));
        }

        // Обновляем роль, если это админ и роль предоставлена
        if (isAdmin && normalizedUpdate.role() != null) {
            existing.setRole(normalizedUpdate.role());
            log.info("Администратор обновил роль пользователя с id={} на {}", id, normalizedUpdate.role());
        }

        UserEntity updated = repository.save(existing);
        log.info("Обновлен пользователь: '{}' с id={}", userUpdate.username(), id);
        return mapper.toDomain(updated);
    }

    public void deleteUser(
            Long id
    ) {
        var user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = " + id + " не найден"));

        if (user.getRole().equals(UserRole.ADMIN)) {
            throw new IllegalStateException("Невозможно удалить администратора");
        }

        repository.deleteById(id);
        log.info("Пользователь с id = {} успешно удален", id);
    }

    // Проверка существования пользователя
    public boolean userExists(String username) {
        return repository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return repository.existsByEmail(email);
    }
}
