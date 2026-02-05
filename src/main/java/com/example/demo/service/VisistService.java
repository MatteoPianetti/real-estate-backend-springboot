package com.example.demo.service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.Visit.Visit;
import com.example.demo.domain.Visit.VisitStatus;
import com.example.demo.domain.property.Property;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.enumerated.Role;
import com.example.demo.dto.visit.VisitRequest;
import com.example.demo.dto.visit.VisitResponse;
import com.example.demo.exception.NoEntityFoundException;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VisitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisistService {
    private final VisitRepository repository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    //creiamo una visita
    public VisitResponse requestVisist(VisitRequest request, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow(() -> new NoEntityFoundException("Nessuno user trovato"));
        Property prop = propertyRepository.findById(request.getPropertyId()).orElseThrow(() -> new NoEntityFoundException("Nessuna proprieta trovata"));
    
        if(request.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La data della visita è nel passato!!");
        }

        Visit visita = Visit.builder()
            .user(user)
            .property(prop)
            .visitDate(request.getDate())
            .status(VisitStatus.PENDING)
            .build();

        repository.save(visita);
        return VisitResponse.from(visita);
    }

    //approvazione della visita
    public void approveVisit(Long visitId, Principal principal) throws AccessDeniedException {
        Visit visita = repository.findById(visitId).
            orElseThrow(() -> new NoEntityFoundException("Nessuna visita trovata con id: " + visitId));
        
        User manager = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new NoEntityFoundException("Manager non trovato!"));

        if(!visita.getProperty().getManager().getId().equals(manager.getId())) {
            throw new AccessDeniedException("Non puoi gestire visiste di proprietà non tue");
        }

        visita.setStatus(VisitStatus.APPROVED);
        visita.setUpdatedAt(LocalDateTime.now());

        repository.save(visita);
    }

    //rifiutiamo la visita
    public void rejectVisit(Long visitId, Principal principal) throws AccessDeniedException {
        Visit visita = repository.findById(visitId).orElseThrow(() -> new NoEntityFoundException("Nessuna visita trovata con id: " + visitId));
        User manager = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new NoEntityFoundException("Manager non trovato!"));

        if(!visita.getProperty().getManager().getId().equals(manager.getId())) {
            throw new AccessDeniedException("Non puoi gestire visiste di proprietà non tue");
        }
        visita.setStatus(VisitStatus.REJECTED);
        visita.setUpdatedAt(LocalDateTime.now());

        repository.save(visita);
    }    

    public List<VisitResponse> findByPropertyId(Long propertyId, Principal principal) throws AccessDeniedException {
        User requester = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new NoEntityFoundException("Manager non trovato!"));
        
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new NoEntityFoundException("Casa non trovata"));
        
        boolean isAdmin = requester.getRole() == Role.ADMIN;
        boolean isOwner = property.getManager().getId().equals(requester.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Non puoi vedere le visite di proprietà non tue");
        }

        List<Visit> visite =  repository.findByPropertyId(propertyId);

        return visite.stream().map(VisitResponse::from).toList();
    }

    public List<VisitResponse> findByUserId(Principal principal) {
        List<Visit> visite =  repository.findByUser_Email(principal.getName());
        if(visite.isEmpty()) {
            throw new NoEntityFoundException("Utente non trovato");
        }

        return visite.stream().map(VisitResponse::from).toList();
    }
}
