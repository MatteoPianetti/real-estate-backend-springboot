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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.property.PropertyRequest;
import com.example.demo.dto.property.PropertyResponse;
import com.example.demo.dto.propertyImage.PropertyImageRequest;
import com.example.demo.dto.propertyImage.PropertyImageResponse;
import com.example.demo.service.PropertyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    // --- CREA NUOVA PROPERTY (MANAGER o ADMIN) ---
    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<PropertyResponse> createProperty(
            @RequestBody @Valid PropertyRequest request,
            Principal principal
    ) {
        PropertyResponse response = propertyService.createProperty(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // --- AGGIORNA PROPERTY (MANAGER o ADMIN) ---
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @RequestBody @Valid PropertyRequest request,
            Principal principal
    ) throws AccessDeniedException {
        PropertyResponse response = propertyService.updateProperty(id, request, principal);
        return ResponseEntity.ok(response);
    }

    // --- CANCELLA PROPERTY (MANAGER o ADMIN) ---
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteProperty(
            @PathVariable Long id,
            Principal principal
    ) throws AccessDeniedException {
        propertyService.deleteProperty(id, principal);
        return ResponseEntity.noContent().build();
    }

    // --- OTTIENI TUTTE LE PROPERTY (PUBBLICO) ---
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> getAllProperties() {
        return ResponseEntity.ok(propertyService.findAll());
    }

    // --- OTTIENI UNA PROPERTY PER ID (PUBBLICO) ---
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.findById(id));
    }

    // --- AGGIUNGI IMMAGINE ALLA PROPERTY (MANAGER o ADMIN) ---
    @PostMapping("/{id}/images")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<PropertyImageResponse> addImage(
            @PathVariable Long id,
            @RequestBody @Valid PropertyImageRequest request,
            Principal principal
    ) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(propertyService.addImageToProperty(id, request, principal));
    }

    // --- ELIMINA IMMAGINE (MANAGER o ADMIN) ---
    @DeleteMapping("/images/{imageId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long imageId,
            Principal principal
    ) throws AccessDeniedException {
        propertyService.deleteImage(imageId, principal);
        return ResponseEntity.noContent().build();
    }

    // --- LISTA IMMAGINI DI UNA PROPERTY (PUBBLICO) ---
    @GetMapping("/{id}/images")
    public ResponseEntity<List<PropertyImageResponse>> getImages(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getImagesByProperty(id));
    }
}

