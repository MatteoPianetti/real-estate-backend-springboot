package com.example.demo.service;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.domain.PropertyImage;
import com.example.demo.domain.property.Property;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.enumerated.Role;
import com.example.demo.dto.property.PropertyRequest;
import com.example.demo.dto.property.PropertyResponse;
import com.example.demo.dto.propertyImage.PropertyImageRequest;
import com.example.demo.dto.propertyImage.PropertyImageResponse;
import com.example.demo.exception.NoEntityFoundException;
import com.example.demo.repository.PropertyImageRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository imageRepository;
    private final UserRepository userRepository;

    // --- CREA PROPERTY ---
    @Transactional
    public PropertyResponse createProperty(PropertyRequest request, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .address(request.getAddress())
                .city(request.getCity())
                .size(request.getSize())
                .rooms(request.getRooms())
                .bathrooms(request.getBathrooms())
                .status(request.getStatus())
                .manager(user)
                .build();

        propertyRepository.save(property);
        return PropertyResponse.from(property);
    }

    // --- AGGIORNA PROPERTY ---
    @Transactional
    public PropertyResponse updateProperty(Long propertyId, PropertyRequest request, Principal principal) throws AccessDeniedException {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NoEntityFoundException("Property non trovata"));

        // Controllo ownership / admin
        if (!property.getManager().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Non puoi modificare questa property");
        }

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setSize(request.getSize());
        property.setRooms(request.getRooms());
        property.setBathrooms(request.getBathrooms());
        property.setStatus(request.getStatus());

        propertyRepository.save(property);
        return PropertyResponse.from(property);
    }

    // --- CANCELLA PROPERTY ---
    @Transactional
    public void deleteProperty(Long propertyId, Principal principal) throws AccessDeniedException {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NoEntityFoundException("Property non trovata"));

        if (!property.getManager().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Non puoi cancellare questa property");
        }

        propertyRepository.delete(property);
    }

    // --- TROVA TUTTE LE PROPERTY ---
    @Transactional
    public List<PropertyResponse> findAll() {
        return propertyRepository.findAll()
                .stream()
                .map(PropertyResponse::from)
                .toList();
    }

    // --- TROVA PROPERTY PER ID ---
    @Transactional
    public PropertyResponse findById(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NoEntityFoundException("Property non trovata"));
        return PropertyResponse.from(property);
    }

    // --- AGGIUNGI IMMAGINE ---
    @Transactional
    public PropertyImageResponse addImageToProperty(Long propertyId, PropertyImageRequest request, Principal principal) throws AccessDeniedException {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NoEntityFoundException("Property non trovata"));

        if (!property.getManager().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Non puoi aggiungere immagini a questa property");
        }

        PropertyImage image = PropertyImage.builder()
                .property(property)
                .url(request.getUrl())
                .cover(request.isCover())
                .build();

        imageRepository.save(image);
        return PropertyImageResponse.from(image);
    }

    // --- ELIMINA IMMAGINE ---
    @Transactional
    public void deleteImage(Long imageId, Principal principal) throws AccessDeniedException {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NoEntityFoundException("Utente non trovato"));

        PropertyImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NoEntityFoundException("Immagine non trovata"));

        Property property = image.getProperty();

        if (!property.getManager().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Non puoi cancellare questa immagine");
        }

        imageRepository.delete(image);
    }

    // --- LISTA IMMAGINI DI UNA PROPERTY ---
    @Transactional
    public List<PropertyImageResponse> getImagesByProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new NoEntityFoundException("Property non trovata"));

        return property.getImages()
                .stream()
                .map(PropertyImageResponse::from)
                .toList();
    }
}
