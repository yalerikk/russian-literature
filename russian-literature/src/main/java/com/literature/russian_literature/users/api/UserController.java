package com.literature.russian_literature.users.api;

import com.literature.russian_literature.users.domain.User;
import com.literature.russian_literature.users.domain.LoginRequest;
import com.literature.russian_literature.users.domain.UserService;
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
    public ResponseEntity<User> getUserById(
            @PathVariable("id") Long id
    ) {
        log.info("Called getUserById by id={}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserById(id));
    }

    // GET ALL
    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Called getAllUsers");
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getAllUsers());
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @Valid @RequestBody User userToCreate
    ) {
        log.info("Called registerUser");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(userToCreate));
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(
            @RequestBody LoginRequest loginRequest
    ) {
        log.info("Called loginUser for user: {}", loginRequest.login());
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    // EDIT
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody User userToUpdate,
            @RequestParam(defaultValue = "false") boolean isAdmin
    ) {
        log.info("Called updateUser id={}, userToUpdate={}, isAdmin={}", id, userToUpdate, isAdmin);
        var updated = userService.updateUser(id, userToUpdate, isAdmin);
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
