package com.example.demo.domain.user.enumerated;

import static com.example.demo.domain.user.enumerated.Permission.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
public enum Role {
    
    USER(
        Set.of(PROPERTY_READ)
    ),
    ADMIN(
        Set.of(
            PROPERTY_CREATE,
            PROPERTY_DELETE,
            PROPERTY_READ,
            PROPERTY_UPDATE,
            USER_MANAGE
        )
    ),

    MANAGER(
        Set.of(
            PROPERTY_CREATE,
            PROPERTY_DELETE,
            PROPERTY_READ,
            PROPERTY_UPDATE
        )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
