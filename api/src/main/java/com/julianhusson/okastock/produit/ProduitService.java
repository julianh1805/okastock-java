package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.CategorieService;
import com.julianhusson.okastock.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public record ProduitService(ProduitRepository produitRepository, CategorieService categorieService) {

    public List<Produit> getAll() {
        return produitRepository.findAll();
    }

    public Produit getById(UUID productId) {
        return produitRepository.findById(productId).orElseThrow(() ->
                new NotFoundException(getNotFoundProduitError(productId)));
    }

    public void upSert(Produit produit, String categorieName) {
        if(produit.getId() == null || produitRepository.existsById(produit.getId())){
            produit.setCategorie(categorieService.findByNom(categorieName));
            produitRepository.save(produit);
        } else {
                throw new NotFoundException(getNotFoundProduitError(produit.getId()));
        }
    }

    public void delete(UUID productId) {
        if(produitRepository.existsById(productId)){
          produitRepository.deleteById(productId);
        } else{
            throw new NotFoundException(getNotFoundProduitError(productId));
        }
    }

    private String getNotFoundProduitError(UUID productId){
        return "Aucun produit n'existe avec l'id " + productId + ".";
    }

}
