package com.notes_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.notes_api.dto.request.LoginRequest;
import com.notes_api.dto.request.RegisterUserRequest;
import com.notes_api.dto.response.UserResponse;
import com.notes_api.model.UserAccount;
import com.notes_api.service.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody RegisterUserRequest request) {
    	// Setting role 2 "USER" by default
        UserAccount savedUser = userService.registerUser(request.email, request.password, 2l);
        UserResponse response = new UserResponse(
        		savedUser.getId(),
        		savedUser.getEmail(),
        		savedUser.getRole() != null ? savedUser.getRole().getName() : null
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        Optional<UserAccount> userOpt = userService.authenticateUser(request.email, request.password);
        
        return userOpt
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole() != null ? user.getRole().getName() : null
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        Optional<UserAccount> userOpt = userService.getUserById(id);
        
        return userOpt
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole() != null ? user.getRole().getName() : null
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

