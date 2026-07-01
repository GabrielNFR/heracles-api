package com.gabrielnfr.heracles_api.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.gabrielnfr.heracles_api.model.Usuario;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String email;
    private String username;
    private String password;

    UserDetailsImpl(Usuario user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.email;  
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired()     { return true; }
    @Override
    public boolean isAccountNonLocked()      { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled()               { return true; }
}
