package de.modulo.backend.repositories;

import de.modulo.backend.entities.UserEntity;
import de.modulo.backend.enums.ROLE;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByMail(String mail);
    List<UserEntity> findAllByRole(ROLE role);
}
