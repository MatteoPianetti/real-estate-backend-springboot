package com.example.demo.service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.offer.Offer;
import com.example.demo.domain.offer.OfferStatus;
import com.example.demo.domain.property.Property;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.enumerated.Role;
import com.example.demo.dto.offer.OfferRequest;
import com.example.demo.dto.offer.OfferResponse;
import com.example.demo.exception.NoEntityFoundException;
import com.example.demo.repository.OfferRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository repo;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    //make offer -> user
    @Transactional
    public OfferResponse createOffer(OfferRequest request, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
            .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        Property prop = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new NoEntityFoundException("Casa non trovata"));

        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Quantità di denaro offerta inaccettabile!");
        }

        Offer o = Offer.builder()
            .user(user)
            .property(prop)
            .amount(request.getAmount())
            .status(OfferStatus.PENDING)
            .build();

        repo.save(o);
        return OfferResponse.from(o);
    }
    
    //accetta -> manager/admin
    @Transactional
    public void acceptdOffer(Long offerId, Principal principal) throws AccessDeniedException {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));
        Offer offerta = repo.findById(offerId)
                .orElseThrow(() -> new NoEntityFoundException("Offerta non trovata"));
        
        if(!offerta.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Non accettare offerte di case non tue!");
        }

        offerta.setStatus(OfferStatus.ACCEPTED);
        offerta.setUpdatedAt(LocalDateTime.now());

        repo.save(offerta);
    }
    
    //rifiuta -> manager/admin
    @Transactional
    public void refuseOffer(Long offerId, Principal principal) throws AccessDeniedException{
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));
        Offer offerta = repo.findById(offerId)
                .orElseThrow(() -> new NoEntityFoundException("Offerta non trovata"));

        if(!offerta.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Non accettare offerte di case non tue!");
        }

        offerta.setStatus(OfferStatus.REJECTED);
        offerta.setUpdatedAt(LocalDateTime.now());

        repo.save(offerta);
    }

    //prendi da proprietà -> manager/admin
    public List<OfferResponse> getAllByPropertyId(Long propertyId, Principal principal) throws AccessDeniedException {
            User manager = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

            Property property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new NoEntityFoundException("Casa non trovata"));

            if (!property.getManager().getId().equals(manager.getId())
                    && manager.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("Non puoi visualizzare offerte di proprietà non tue");
            }

            return repo.findAllByProperty_Id(propertyId)
                    .stream()
                    .map(OfferResponse::from)
                    .toList();
        

    }

    //prendi da utente -> user
    public List<OfferResponse> getAllByUserId(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));
        List<Offer> offerte = repo.findAllByUser_Id(user.getId());

        return offerte.stream().map(OfferResponse::from).toList();
    }
}
