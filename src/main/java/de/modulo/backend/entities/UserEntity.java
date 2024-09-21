package de.modulo.backend.entities;

import de.modulo.backend.enums.ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users") // Specifies the table name
@Data // Generates getters, setters, equals, hashcode, and toString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields
public class UserEntity implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    @Column(name = "mail", unique = true, nullable = false) // Unique and not null
    private String mail;

    @Column(name = "title") // No additional constraints, optional field
    private String title;

    @Column(name = "first_name", nullable = false) // Not null
    private String firstName;

    @Column(name = "last_name", nullable = false) // Not null
    private String lastName;

    @Column(name = "code", nullable = false) // Not null
    private String code;

    @Column(name = "password")
    private String password;

    @Column(name = "role", nullable = false) // Not null
    private ROLE role;

    @Column(name = "expired", nullable = false) // Not null
    private boolean expired = false;

    @Column(name = "locked", nullable = false) // Not null
    private boolean locked = false;

    @Column(name = "credentials_expired", nullable = false) // Not null
    private boolean credentialsExpired = false;

    @Column(name = "enabled", nullable = false) // Not null
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return "";
    }
}

