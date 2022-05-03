package com.julianhusson.okastock.mapstruct.mapper;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.mapstruct.dto.ProduitDTO;
import com.julianhusson.okastock.mapstruct.dto.ProduitPostDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurProduitDTO;
import com.julianhusson.okastock.produit.Produit;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ProduitMapperTest {

    @Autowired
    private ProduitMapper underTest;

    @Test
    void produitToProduitDTO() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "https://test.com", UUID.randomUUID().toString(), true, true, "test@test.com", "1234AZER", null, null);
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>());
        Produit produit = new Produit(UUID.fromString("e59ed17d-db7d-4d24-af6c-5154b3f72df0"), "Titre", "Petite description", new BigDecimal("10.27"), 8, categorie, utilisateur);
        //When
        ProduitDTO produitDTO = underTest.produitToProduitDTO(produit);
        //Then
        assertThat(produitDTO.id()).isEqualTo(produit.getId());
        assertThat(produitDTO.titre()).isEqualTo(produit.getTitre());
        assertThat(produitDTO.description()).isEqualTo(produit.getDescription());
        assertThat(produitDTO.prix()).isEqualTo(produit.getPrix().doubleValue());
        assertThat(produitDTO.quantite()).isEqualTo(produit.getQuantite());
        assertThat(produitDTO.categorie()).isEqualTo(produit.getCategorie().getNom());
        assertThat(produitDTO.createdAt()).isEqualTo(produit.getCreatedAt());
        assertThat(produitDTO.updatedAt()).isEqualTo(produit.getUpdatedAt());
    }

    @Test
    void produitPostDTOToProduit() {
        //Given
        ProduitPostDTO produitPostDTO = new ProduitPostDTO("Titre", "Petite description", 10.27, 8, "meubles");
        //When
        Produit produit = underTest.produitPostDTOToProduit(produitPostDTO);
        //Then
        assertThat(produit.getTitre()).isEqualTo(produitPostDTO.titre());
        assertThat(produit.getDescription()).isEqualTo(produitPostDTO.description());
        assertThat(produit.getPrix().doubleValue()).isEqualTo(produitPostDTO.prix());
        assertThat(produit.getQuantite()).isEqualTo(produitPostDTO.quantite());
        assertThat(produit.getCategorie()).isNull();
    }

    @Test
    void utilisateurToUtilisateurDTO() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "https://test.com", UUID.randomUUID().toString(),true, true, "test@test.com", "1234AZER", null, null);
        //When
        UtilisateurProduitDTO utilisateurProduitDTO = underTest.utilisateurToUtilisateurProduitDTO(utilisateur);
        //Then
        assertThat(utilisateurProduitDTO.nom()).isEqualTo(utilisateur.getNom());
        assertThat(utilisateurProduitDTO.siret()).isEqualTo(utilisateur.getSiret());
        assertThat(utilisateurProduitDTO.codePostal()).isEqualTo(utilisateur.getCodePostal());
        assertThat(utilisateurProduitDTO.telephone()).isEqualTo(utilisateur.getTelephone());
        assertThat(utilisateurProduitDTO.site()).isEqualTo(utilisateur.getSite());
        assertThat(utilisateurProduitDTO.logo()).isEqualTo(utilisateur.getLogo());
        assertThat(utilisateurProduitDTO.rgpd()).isEqualTo(utilisateur.isRgpd());
        assertThat(utilisateurProduitDTO.email()).isEqualTo(utilisateur.getEmail());
    }
}