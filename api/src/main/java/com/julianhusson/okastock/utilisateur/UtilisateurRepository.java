package com.julianhusson.okastock.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Utilisateur findByEmail(String email);
}
