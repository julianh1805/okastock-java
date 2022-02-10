package com.julianhusson.okastock.mapstruct.mappers;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.mapstruct.dtos.CategorieDTO;
import com.julianhusson.okastock.mapstruct.dtos.CategorieProduitDTO;
import com.julianhusson.okastock.produit.Produit;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
class CategorieMapperTest {

    @Autowired
    private CategorieMapper underTest;

    @Test
    void categorieToCategorieDTO() {
        //Given
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>());
        Set<Produit> produits = new HashSet<>();
        produits.add(new Produit(UUID.fromString("e59ed17d-db7d-4d24-af6c-5154b3f72df0"), "Titre", "Petite description", 10.27, 8, categorie));
        categorie.setProduits(produits);
        //When
        CategorieDTO categorieDTO = underTest.categorieToCategorieDTO(categorie);
        //Then
        assertThat(categorieDTO.getId()).isEqualTo(categorie.getId());
        assertThat(categorieDTO.getNom()).isEqualTo(categorie.getNom());
        assertThat(categorieDTO.getProduits().size()).isPositive();
    }

    @Test
    void listProduitsToListCategorieProduitsDTO() {
        //Given
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>());
        Set<Produit> produits = new HashSet<>();
        produits.add(new Produit(UUID.fromString("e59ed17d-db7d-4d24-af6c-5154b3f72df0"), "Titre", "Petite description", 10.27, 8, categorie));
        //When
        Set<CategorieProduitDTO> produitDTOS = underTest.listProduitsToListCategorieProduitsDTO(produits);
        //Then
        assertThat(produitDTOS.stream().findFirst().get().getId()).isEqualTo(produits.stream().findFirst().get().getId());
        assertThat(produitDTOS.stream().findFirst().get().getTitre()).isEqualTo(produits.stream().findFirst().get().getTitre());
        assertThat(produitDTOS.stream().findFirst().get().getDescription()).isEqualTo(produits.stream().findFirst().get().getDescription());
        assertThat(produitDTOS.stream().findFirst().get().getPrix()).isEqualTo(produits.stream().findFirst().get().getPrix());
        assertThat(produitDTOS.stream().findFirst().get().getQuantite()).isEqualTo(produits.stream().findFirst().get().getQuantite());
    }
}