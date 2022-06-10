package com.example.demo.models.recruiter;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class RecruiterPrincipal implements UserDetails {
    private Recruiter recruiter;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        this.recruiter.getRoles().forEach(role -> {
            roles.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())));
        });
        return roles;
    }

    @Override
    public String getPassword() {
        return this.recruiter.getPassword();
    }

    @Override
    public String getUsername() {
        return this.recruiter.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
