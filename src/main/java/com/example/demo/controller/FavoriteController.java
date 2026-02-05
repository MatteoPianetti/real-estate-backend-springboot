package com.example.demo.controller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.favorite.FavoriteRequest;
import com.example.demo.service.FavoriteService;
import com.example.demo.dto.favorite.FavoriteResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoriteController {
    private final FavoriteService service;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FavoriteResponse> addFavorite(
        @RequestBody @Valid FavoriteRequest request,
        Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addNewFavorite(request, principal));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteFavorite(
        @PathVariable Long id,
        Principal principal
    ) throws AccessDeniedException{
        service.deleteFavorite(id, principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public List<FavoriteResponse> getAll(
        Principal principal
    ) {
        return service.findAllByUserId(principal);
    }
}
