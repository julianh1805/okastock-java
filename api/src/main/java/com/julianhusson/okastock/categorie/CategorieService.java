package com.julianhusson.okastock.categorie;

import com.julianhusson.okastock.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record CategorieService(CategorieRepository categorieRepository) {

    public Categorie findByNom(String nom) {
        Optional<Categorie> categorie = categorieRepository.findByNom(nom);
        return categorie.orElseThrow(() -> new NotFoundException("La categorie '" + nom + "' n'existe pas"));
    }
}
