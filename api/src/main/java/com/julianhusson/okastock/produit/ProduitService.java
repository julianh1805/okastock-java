package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.CategorieService;
import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.security.AuthenticationFacade;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public record ProduitService(ProduitRepository produitRepository, CategorieService categorieService, UtilisateurService utilisateurService, AuthenticationFacade authenticationFacade) {

    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    public Produit getById(UUID productId) {
        return produitRepository.findById(productId).orElseThrow(() ->
                new NotFoundException(getNotFoundProduitError(productId)));
    }

    public void create(Produit produit, String categorieName) {
            produit.setUtilisateur(authenticationFacade.getAuthenticatedUser());
            produit.setCategorie(categorieService.findByNom(categorieName));
            produitRepository.save(produit);
    }

    public void update(Produit produit, String categorieName) {
        if(produitRepository.existsById(produit.getId())){
            produit.setCategorie(categorieService.findByNom(categorieName));
            produitRepository.save(produit);
        } else {
            throw new NotFoundException(getNotFoundProduitError(produit.getId()));
        }
    }

    public void delete(UUID productId) {
        this.getById(productId);
        produitRepository.deleteById(productId);
    }

    private String getNotFoundProduitError(UUID productId){
        return "Aucun produit n'existe avec l'id " + productId + ".";
    }

}
