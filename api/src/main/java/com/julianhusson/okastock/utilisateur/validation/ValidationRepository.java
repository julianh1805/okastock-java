package com.julianhusson.okastock.utilisateur.validation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ValidationRepository extends JpaRepository<ValidationToken, UUID> {
    Optional<ValidationToken> findByToken(String token);
    void deleteAllByUtilisateurId(UUID userId);
}
