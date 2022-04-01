package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.CategorieService;
import com.julianhusson.okastock.exception.BadUserException;
import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import com.julianhusson.okastock.utils.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public record ProduitService(ProduitRepository produitRepository, CategorieService categorieService, UtilisateurService utilisateurService) {

    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    public Produit getById(UUID produitId) {
        return produitRepository.findById(produitId).orElseThrow(() ->
                new NotFoundException("Aucun produit n'existe avec l'id " + produitId + "."));
    }

    public void create(Produit produit, String categorieName, String userId) {
            produit.setUtilisateur(utilisateurService.getById(UUID.fromString(userId)));
            produit.setCategorie(categorieService.findByNom(categorieName));
            produitRepository.save(produit);
    }

    public void update(Produit produit, String categorieName) {
        Produit produitToUpdate = this.getById(produit.getId());
        Utils.checkAuthUser(produitToUpdate.getUtilisateur().getId());
        produit.setCategorie(categorieService.findByNom(categorieName));
        produitRepository.save(produit);
    }

    public void delete(UUID produitId) {
        Produit produit = this.getById(produitId);
        Utils.checkAuthUser(produit.getUtilisateur().getId());
        produitRepository.deleteById(produitId);
    }

}
