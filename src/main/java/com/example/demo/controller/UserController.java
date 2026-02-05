package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.user.ChangePasswordRequest;
import com.example.demo.domain.user.enumerated.Role;
import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
        @RequestBody ChangePasswordRequest request,
        Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest request) {
        UserResponse response = service.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } 

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/everybody")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/registerAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUserAsAdmin(
        @RequestBody @Valid UserRequest request,
        @RequestParam Role role
    ) {
        UserResponse response = service.createUserAsAdmin(request, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateuserRole(
        @PathVariable Long id,
        @RequestParam Role role
    ) {
        service.updateRole(role, id);
        return ResponseEntity.noContent().build();
    }

    // ==========================
    // TROVA UTENTE PER EMAIL (UTENTE CONNESSO O ADMIN)
    // =========================
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        String email = principal.getName();
        UserResponse response = service.findByEmail(email);
        return ResponseEntity.ok(response);
    }
}
