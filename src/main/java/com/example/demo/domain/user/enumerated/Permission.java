package com.example.demo.domain.user.enumerated;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
public enum Permission {
    PROPERTY_READ("property:read"),
    PROPERTY_UPDATE("property:update"),
    PROPERTY_CREATE("property:create"),
    PROPERTY_DELETE("property:delete"),

    USER_MANAGE("user:manage")
    ;

    @Getter
    private final String permission;
}
