package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.categorie.CategorieService;
import com.julianhusson.okastock.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public record ProduitService(ProduitRepository produitRepository,
                             CategorieService categorieService) {

    private static final String notFoundProduitError = "Aucun produit n'existe avec l'id ";

    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    public Produit getById(UUID productId) {
        return produitRepository.findById(productId).orElseThrow(() ->
                new NotFoundException(notFoundProduitError + productId + "."));
    }

    public void add(Produit produit) {
        Categorie categorie = categorieService.findByNom(produit.getCategorie().getNom());
        produit.setCategorie(categorie);
        produitRepository.save(produit);
    }

    public void update(Produit produit) {
        if(produitRepository.existsById(produit.getId())){
            produit.setCategorie(categorieService.findByNom(produit.getCategorie().getNom()));
            produitRepository.save(produit);
        } else{
            throw new NotFoundException(notFoundProduitError + produit.getId() + ".");
        }
    }

    public void delete(UUID productId) {
        if(produitRepository.existsById(productId)){
          produitRepository.deleteById(productId);
        } else{
            throw new NotFoundException(notFoundProduitError + productId + ".");
        }
    }
}
