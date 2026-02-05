package com.example.demo.controller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.visit.VisitRequest;
import com.example.demo.dto.visit.VisitResponse;
import com.example.demo.service.VisistService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class VisitController {
    private final VisistService service;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<VisitResponse> createVisit(
        @RequestBody @Valid VisitRequest request,
        Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.requestVisist(request, principal));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> approveVisit(
        @PathVariable Long id,
        Principal principal
    ) throws AccessDeniedException {
        service.approveVisit(id, principal);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/rejected")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> rejectVisit(
        @PathVariable Long id,
        Principal principal
    ) throws AccessDeniedException{
        service.rejectVisit(id,principal);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/property/{propertyId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<List<VisitResponse>> getVisitsByPropertyId(
        @PathVariable Long propertyId,
        Principal principal
    ) throws AccessDeniedException {
        List<VisitResponse> visite = service.findByPropertyId(propertyId, principal);
        return ResponseEntity.ok(visite);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public List<VisitResponse> getVisitsByUser(Principal principal) {
       return service.findByUserId(principal);
    }
}
