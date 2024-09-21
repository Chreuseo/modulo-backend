package de.modulo.backend.enums;

import org.springframework.security.core.GrantedAuthority;

public enum ROLE implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
