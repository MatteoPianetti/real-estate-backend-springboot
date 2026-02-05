package com.example.demo.service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.favorite.FavoriteRequest;
import com.example.demo.dto.favorite.FavoriteResponse;
import com.example.demo.exception.NoEntityFoundException;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.demo.domain.Favorite;
import com.example.demo.domain.property.Property;
import com.example.demo.domain.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository repository;
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    @Transactional
    public FavoriteResponse addNewFavorite(FavoriteRequest request, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        Property prop = propertyRepository.findById(request.getPropertyId())
                .orElseThrow(() -> new NoEntityFoundException("Casa non trovata"));

        Favorite f = Favorite.builder()
            .user(user)
            .property(prop)
            .build();
        
        repository.save(f);

        return FavoriteResponse.from(f);
    }

    @Transactional
    public void deleteFavorite(Long favoriteId, Principal principal) throws AccessDeniedException{
        Favorite f = repository.findById(favoriteId)
                .orElseThrow(() -> new NoEntityFoundException("Favorito non trovato"));
        
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        if(!f.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Non puoi cancellare i preferiti di un altro utente!!");
        }

        repository.delete(f);
    }

    public List<FavoriteResponse> findAllByUserId(Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));
        List<Favorite> preferiti = repository.findAllByUserId(user.getId());

        return preferiti.stream().map(FavoriteResponse::from).toList();
    }
}
