package com.julianhusson.okastock.categorie;

import com.julianhusson.okastock.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public record CategorieService(CategorieRepository categorieRepository) {

    public Categorie findByNom(String nom) {
        return categorieRepository.findByNom(nom).orElseThrow(() ->
                new NotFoundException("La categorie '" + nom + "' n'existe pas"));
    }
}
