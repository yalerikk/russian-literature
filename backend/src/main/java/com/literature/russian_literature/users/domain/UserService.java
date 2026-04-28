package com.literature.russian_literature.users.domain;

import com.literature.russian_literature.ratings.db.BookRatingRepository;
import com.literature.russian_literature.security.SecurityUtils;
import com.literature.russian_literature.userbooks.db.UserBookRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserNormalizer userNormalizer;
    private final UserValidator userValidator;
    private final UserBookRepository userBookRepository;
    private final BookRatingRepository bookRatingRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
                       UserNormalizer userNormalizer, UserValidator userValidator,
                       UserBookRepository userBookRepository, BookRatingRepository bookRatingRepository
    ) {
        this.repository = userRepository;
        this.mapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userNormalizer = userNormalizer;
        this.userValidator = userValidator;
        this.userBookRepository = userBookRepository;
        this.bookRatingRepository = bookRatingRepository;
    }

    public User getUserById(Long id) {
        UserEntity userEntity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + id + " not found"));
        return mapper.toDomain(userEntity);
    }

    public Page<UserEntity> getAllUsersForAdmin(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public User createUser(User userToCreate) {
        User normalizedUser = userNormalizer.normalizeUser(userToCreate);
        userValidator.validateForCreate(normalizedUser);

        var entityToSave = mapper.toEntity(normalizedUser);
        entityToSave.setRole(UserRole.READER);
        entityToSave.setPassword(passwordEncoder.encode(entityToSave.getPassword()));

        var savedEntity = repository.save(entityToSave);
        LOG.info("Created user: '{}' with id = {}", savedEntity.getUsername(), savedEntity.getId());
        return mapper.toDomain(savedEntity);
    }

    public User loginUser(LoginRequest loginRequest) {
        UserEntity userEntity = repository.findByUsername(loginRequest.login())
                .orElseGet(() -> repository.findByEmail(loginRequest.login())
                        .orElseThrow(() -> new EntityNotFoundException("User not found")));

        if (!passwordEncoder.matches(loginRequest.password(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        LOG.info("User logged in: '{}' with id = {}", userEntity.getUsername(), userEntity.getId());
        return mapper.toDomain(userEntity);
    }

    @Transactional
    public User updateUser(Long id, User userUpdate) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = repository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isAdmin && !currentUserId.equals(id)) {
            throw new IllegalStateException("You can only edit your own profile");
        }

        UserEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        User normalizedUpdate = userNormalizer.normalizeUser(userUpdate);
        userValidator.validateForUpdate(id, normalizedUpdate);

        if (normalizedUpdate.username() != null && !normalizedUpdate.username().equals(existing.getUsername())) {
            existing.setUsername(normalizedUpdate.username());
        }
        if (normalizedUpdate.email() != null && !normalizedUpdate.email().equals(existing.getEmail())) {
            existing.setEmail(normalizedUpdate.email());
        }
        if (normalizedUpdate.password() != null) {
            existing.setPassword(passwordEncoder.encode(normalizedUpdate.password()));
        }
        if (isAdmin && normalizedUpdate.role() != null) {
            existing.setRole(normalizedUpdate.role());
            LOG.info("Admin updated role for user id={} to {}", id, normalizedUpdate.role());
        }

        UserEntity updated = repository.save(existing);
        LOG.info("Updated user: '{}' with id={}", updated.getUsername(), id);
        return mapper.toDomain(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        UserEntity currentUser = repository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("Current user not found"));
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;

        if (!isAdmin && !currentUserId.equals(id)) {
            throw new IllegalStateException("You can only delete your own account");
        }

        UserEntity user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id = " + id + " not found"));

        if (user.getRole() == UserRole.ADMIN) {
            long adminCount = repository.countByRole(UserRole.ADMIN);
            if (adminCount <= 1) {
                throw new IllegalStateException("Cannot delete the last admin user");
            }
            if (!isAdmin) {
                throw new IllegalStateException("Only an admin can delete another admin");
            }
        }

        userBookRepository.deleteByUserId(id);
        bookRatingRepository.deleteByUserId(id);
        repository.deleteById(id);
        LOG.info("User with id = {} successfully deleted", id);
    }

    public UserEntity getUserEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
