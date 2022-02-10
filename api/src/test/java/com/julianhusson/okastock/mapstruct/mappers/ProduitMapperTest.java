package com.julianhusson.okastock.mapstruct.mappers;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.mapstruct.dtos.ProduitDTO;
import com.julianhusson.okastock.mapstruct.dtos.ProduitPostDTO;
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
class ProduitMapperTest {

    @Autowired
    private ProduitMapper underTest;

    @Test
    void produitToProduitDTO() {
        //Given
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>());
        Produit produit = new Produit(UUID.fromString("e59ed17d-db7d-4d24-af6c-5154b3f72df0"), "Titre", "Petite description", 10.27, 8, categorie);
        //When
        ProduitDTO produitDTO = underTest.produitToProduitDTO(produit);
        //Then
        assertThat(produitDTO.getId()).isEqualTo(produit.getId());
        assertThat(produitDTO.getTitre()).isEqualTo(produit.getTitre());
        assertThat(produitDTO.getDescription()).isEqualTo(produit.getDescription());
        assertThat(produitDTO.getPrix()).isEqualTo(produit.getPrix());
        assertThat(produitDTO.getQuantite()).isEqualTo(produit.getQuantite());
        assertThat(produitDTO.getCategorie()).isEqualTo(produit.getCategorie().getNom());
        assertThat(produitDTO.getCreatedAt()).isEqualTo(produit.getCreatedAt());
        assertThat(produitDTO.getUpdatedAt()).isEqualTo(produit.getUpdatedAt());
    }

    @Test
    void produitPostDTOToProduit() {
        //Given
        ProduitPostDTO produitPostDTO = new ProduitPostDTO();
        produitPostDTO.setTitre("Titre");
        produitPostDTO.setDescription("Petite description");
        produitPostDTO.setPrix(10.27);
        produitPostDTO.setQuantite(8);
        produitPostDTO.setCategorie("meubles");
        //When
        Produit produit = underTest.produitPostDTOToProduit(produitPostDTO);
        //Then
        assertThat(produit.getTitre()).isEqualTo(produitPostDTO.getTitre());
        assertThat(produit.getDescription()).isEqualTo(produitPostDTO.getDescription());
        assertThat(produit.getPrix()).isEqualTo(produitPostDTO.getPrix());
        assertThat(produit.getQuantite()).isEqualTo(produitPostDTO.getQuantite());
        assertThat(produit.getCategorie()).isNull();

    }
}