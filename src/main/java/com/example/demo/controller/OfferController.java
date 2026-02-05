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

import com.example.demo.dto.offer.OfferRequest;
import com.example.demo.dto.offer.OfferResponse;
import com.example.demo.service.OfferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/offers")
public class OfferController {
       private final OfferService offerService;

    // --- CREARE OFFERTA (solo USER) ---
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OfferResponse> createOffer(
            @RequestBody @Valid OfferRequest request,
            Principal principal
    ) {
        OfferResponse response = offerService.createOffer(request, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // --- ACCETTARE OFFERTA (MANAGER o ADMIN) ---
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> acceptOffer(
            @PathVariable("id") Long offerId,
            Principal principal
    ) throws AccessDeniedException {
        offerService.acceptdOffer(offerId, principal);
        return ResponseEntity.noContent().build();
    }

    // --- RIFIUTARE OFFERTA (MANAGER o ADMIN) ---
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<Void> rejectOffer(
            @PathVariable("id") Long offerId,
            Principal principal
    ) throws AccessDeniedException{
        offerService.refuseOffer(offerId, principal);
        return ResponseEntity.noContent().build();
    }

    // --- LISTA OFFERTE DI UNA PROPERTY (MANAGER/ADMIN) ---
    @GetMapping("/property/{propertyId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<List<OfferResponse>> getOffersByProperty(
            @PathVariable Long propertyId,
            Principal principal
    ) throws AccessDeniedException {
        List<OfferResponse> offers = offerService.getAllByPropertyId(propertyId, principal);
        return ResponseEntity.ok(offers);
    }

    // --- LISTA OFFERTE DELL'UTENTE LOGGATO (USER) ---
    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<OfferResponse>> getMyOffers(Principal principal) {
        List<OfferResponse> offers = offerService.getAllByUserId(principal);
        return ResponseEntity.ok(offers);
    }
}
