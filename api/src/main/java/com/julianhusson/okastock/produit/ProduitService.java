package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.CategorieService;
import com.julianhusson.okastock.exception.BadUserException;
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

    public Produit getById(UUID produitId) {
        return produitRepository.findById(produitId).orElseThrow(() ->
                new NotFoundException("Aucun produit n'existe avec l'id " + produitId + "."));
    }

    public void create(Produit produit, String categorieName) {
            produit.setUtilisateur(authenticationFacade.getAuthenticatedUser());
            produit.setCategorie(categorieService.findByNom(categorieName));
            produitRepository.save(produit);
    }

    public void update(Produit produit, String categorieName) {
        Produit produitToUpdate = this.getById(produit.getId());
        this.checkUser(produitToUpdate.getUtilisateur().getId());
        if(!produitToUpdate.getCategorie().getNom().equals(categorieName)) { produit.setCategorie(categorieService.findByNom(categorieName)); }
        produitRepository.save(produit);
    }

    public void delete(UUID produitId) {
        Produit produit = this.getById(produitId);
        this.checkUser(produit.getUtilisateur().getId());
        produitRepository.deleteById(produitId);
    }

    private void checkUser(UUID utilisateurId){
        if(!utilisateurId.equals(authenticationFacade.getAuthenticatedUser().getId())){
            throw new BadUserException("Vous n'avez pas les droits pour modifier ce produit.");
        }
    }

}
