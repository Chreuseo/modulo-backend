package de.modulo.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOAuth implements UserDetails {
    private String mail;
    private String password;
    private boolean expired;
    private boolean locked;
    private boolean credentialsExpired;
    private boolean enabled;

    // Override the methods from UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return any roles or authorities for the user
        return Collections.emptyList(); // Modify this if you have roles/authorities
    }

    @Override
    public String getUsername() {
        return mail; // Assuming you log in with email
    }

    @Override
    public boolean isAccountNonExpired() {
        return !expired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
