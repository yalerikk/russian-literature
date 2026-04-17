package com.literature.russian_literature.users.api;

import com.literature.russian_literature.users.domain.dto.User;
import com.literature.russian_literature.users.domain.dto.LoginRequest;
import com.literature.russian_literature.users.domain.UserService;
import com.literature.russian_literature.users.domain.dto.UserResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("id") Long id
    ) {
        log.info("Called getUserById by id={}", id);
        User user = userService.getUserById(id);
        UserResponse response = new UserResponse(user.id(), user.username(), user.email(), user.role());
        return ResponseEntity.ok(response);
    }

    // GET ALL
    @GetMapping()
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("Called getAllUsers");
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream()
                .map(u -> new UserResponse(u.id(), u.username(), u.email(), u.role()))
                .toList();
        return ResponseEntity.ok(responses);
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody User userToCreate
    ) {
        log.info("Called registerUser");
        User created = userService.createUser(userToCreate);
        UserResponse response = new UserResponse(created.id(), created.username(), created.email(), created.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(
            @RequestBody LoginRequest loginRequest
    ) {
        log.info("Called loginUser for user: {}", loginRequest.login());
        User user = userService.loginUser(loginRequest);
        UserResponse response = new UserResponse(user.id(), user.username(), user.email(), user.role());
        return ResponseEntity.ok(response);
    }

    // EDIT
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody User userToUpdate
    ) {
        log.info("Called updateUser id={}, userToUpdate={}", id, userToUpdate);
        var updated = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id") Long id
    ) {
        log.info("Called deleteUser: id={}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok()
                .build();
    }
}
