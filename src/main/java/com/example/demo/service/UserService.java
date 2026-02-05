package com.example.demo.service;

import java.security.Principal;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.user.ChangePasswordRequest;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.enumerated.Role;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.exception.NoEntityFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        //la password corrente è giusta??
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Password errata");
        }

        //le due password corrispondono??
        if(!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Le password non sono uguali");
        }

        //aggiorniamo la password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        //salviamo la nuova password
        repository.save(user);
    }

    //Registrazione user normale
    @Transactional
    public UserResponse registerUser(UserRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .build();

        repository.save(user);

        return UserResponse.from(user);
    }

    //Creazione utente da parte dell'admin
    @Transactional
    public UserResponse createUserAsAdmin(UserRequest request, Role role) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(role)
                .build();

        repository.save(user);

        return UserResponse.from(user);
    }

    public UserResponse findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NoEntityFoundException("Nessun utente trovato con id: " + id));
        
        return UserResponse.from(user);
    }

    public List<UserResponse> findAll() {
        List<User> utenti = repository.findAll();

        if(utenti.isEmpty()) {throw new NoEntityFoundException("Non ci sono utenti");}

        return utenti.stream().map(UserResponse::from).toList();
    }

    public void deleteUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NoEntityFoundException("Nessun utente trovato con id: " + id));

        repository.delete(user);
    }

    public void updateRole(Role role, Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NoEntityFoundException("Nessun utente trovato con id: " + userId));

        user.setRole(role);
        repository.save(user);
    }

    public UserResponse findByEmail(String userEmail) {
        User user = repository.findByEmail(userEmail)
                .orElseThrow(() -> new NoEntityFoundException("Nessun utente trovato con email: " + userEmail));
        
        return UserResponse.from(user);
    }

    @Transactional
    public void testRegister() {
        User user = User.builder()
                .email("test@example.com")
                .password(passwordEncoder.encode("Password123!"))
                .firstName("Test")
                .lastName("User")
                .role(Role.USER)
                .build();
        repository.save(user);

        System.out.println("Role salvato: " + user.getRole()); // Deve stampare USER
    }

}
