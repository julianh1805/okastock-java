package com.julianhusson.okastock.categorie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, UUID> {
    Optional<Categorie> findByNom(String nom);
}
