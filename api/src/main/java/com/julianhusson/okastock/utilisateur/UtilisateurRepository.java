package com.julianhusson.okastock.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Optional<Utilisateur> findByEmail(String email);
    boolean existsBySiret(Long siret);
    boolean existsByEmail(String email);
}
