package com.literature.russian_literature.users.api;

import com.literature.russian_literature.security.JwtUtil;
import com.literature.russian_literature.security.SecurityUtils;
import com.literature.russian_literature.users.db.UserEntity;
import com.literature.russian_literature.users.domain.dto.User;
import com.literature.russian_literature.users.domain.dto.LoginRequest;
import com.literature.russian_literature.users.domain.UserService;
import com.literature.russian_literature.users.domain.dto.UserResponse;

import com.literature.russian_literature.users.domain.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/admin/list")
    public ResponseEntity<Page<UserResponse>> getUsersForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> userPage = userService.getAllUsersForAdmin(pageable);
        Page<UserResponse> dtoPage = userPage.map(user -> new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
        LOG.info("Admin list: page={}, size={}, total={}", page, size, dtoPage.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("id") Long id
    ) {
        LOG.info("Get user by id={}", id);
        User user = userService.getUserById(id);
        UserResponse response = new UserResponse(user.id(), user.username(), user.email(), user.role());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody User userToCreate
    ) {
        User created = userService.createUser(userToCreate);
        LOG.info("Registered user with id={}", created.id());
        UserResponse response = new UserResponse(created.id(), created.username(), created.email(), created.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(
            @RequestBody LoginRequest loginRequest
    ) {
        User user = userService.loginUser(loginRequest); // проверяет пароль и возвращает User
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.username())
                .password("") // пароль не нужен для генерации токена, уже проверен
                .authorities("ROLE_" + user.role())
                .build();
        String token = jwtUtil.generateToken(userDetails, user.id());
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        LOG.info("Logged in user: {}", user.username());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserUpdateRequest updateRequest
    ) {
        User updated = userService.updateUser(id, updateRequest);
        LOG.info("Updated user id={}", updated.id());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id
    ) {
        userService.deleteUser(id);
        LOG.info("Deleted user id={}", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(new UserResponse(user.id(), user.username(), user.email(), user.role()));
    }
}
