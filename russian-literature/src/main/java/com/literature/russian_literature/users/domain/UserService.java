package com.literature.russian_literature.users.domain;

import com.literature.russian_literature.security.SecurityUtils;
import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.users.db.UserMapper;
import com.literature.russian_literature.users.db.UserRepository;

import com.literature.russian_literature.users.domain.dto.LoginRequest;
import com.literature.russian_literature.users.domain.dto.User;
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
        UserEntity userEntity = repository.findByUsername(loginRequest.login())
                .orElseGet(() -> repository.findByEmail(loginRequest.login())
                        .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));

        if (!passwordEncoder.matches(loginRequest.password(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Неверный пароль");
        }

        log.info("Авторизовался пользователь: '{}' с id = {}", userEntity.getUsername(), userEntity.getId());
        return mapper.toDomain(userEntity);
    }

    @Transactional
    public User updateUser(Long id, User userUpdate) {
        // 1. Получаем текущего пользователя из SecurityContext
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = repository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        // 2. Проверяем, что пользователь может редактировать этот профиль
        if (!isAdmin && !currentUserId.equals(id)) {
            throw new IllegalStateException("Вы можете редактировать только свой профиль");
        }

        // 3. Загружаем обновляемого пользователя
        UserEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        User normalizedUpdate = userNormalizer.normalizeUser(userUpdate);
        userValidator.validateForUpdate(id, normalizedUpdate);

        // 4. Обновляем поля
        if (normalizedUpdate.username() != null && !normalizedUpdate.username().equals(existing.getUsername())) {
            existing.setUsername(normalizedUpdate.username());
        }
        if (normalizedUpdate.email() != null && !normalizedUpdate.email().equals(existing.getEmail())) {
            existing.setEmail(normalizedUpdate.email());
        }
        if (normalizedUpdate.password() != null) {
            existing.setPassword(passwordEncoder.encode(normalizedUpdate.password()));
        }
        // Обновление роли – только для администратора
        if (isAdmin && normalizedUpdate.role() != null) {
            existing.setRole(normalizedUpdate.role());
            log.info("Администратор обновил роль пользователя с id={} на {}", id, normalizedUpdate.role());
        }

        UserEntity updated = repository.save(existing);
        log.info("Обновлен пользователь: '{}' с id={}", updated.getUsername(), id);
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = repository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isAdmin && !currentUserId.equals(id)) {
            throw new IllegalStateException("Вы можете удалить только свой аккаунт");
        }

        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id = " + id + " не найден"));

        if (user.getRole() == UserRole.ADMIN) {
            long adminCount = repository.countByRole(UserRole.ADMIN);
            if (adminCount <= 1) {
                throw new IllegalStateException("Невозможно удалить последнего администратора");
            }
            if (!isAdmin) {
                throw new IllegalStateException("Только администратор может удалить другого администратора");
            }
        }

        repository.deleteById(id);
        log.info("Пользователь с id = {} успешно удален", id);
    }

    public UserEntity getUserEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }
}
