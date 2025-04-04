package edu.shev.myApp.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER, ADMIN, DEV;

    @Override
    public String getAuthority() {
        return name();
    }
}
