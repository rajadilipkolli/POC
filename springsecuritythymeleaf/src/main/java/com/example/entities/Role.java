package com.example.entities;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority
{

    ROLE_ADMIN,
    ROLE_USER,
    ROLE_ACTUATOR;
    
    @Override
    public String getAuthority()
    {
        return name();
    }

}
