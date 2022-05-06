package com.julianhusson.okastock.mapstruct.mapper;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.mapstruct.dto.ProduitDTO;
import com.julianhusson.okastock.mapstruct.dto.ProduitUtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.produit.Produit;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class UtilisateurMapperTest {

    @Autowired
    private UtilisateurMapper underTest;

    @Test
    void utilisateurPostDTOToUtilisateur() {
        //Given
        UtilisateurPostDTO utilisateurPostDTO = new UtilisateurPostDTO("Test", 12345678910111L, 44300, 666666666L, "https://test.com", true, "test@test.com", "1234AZER");
        //When
        Utilisateur utilisateur = underTest.utilisateurPostDTOToUtilisateur(utilisateurPostDTO);
        //Then
        assertThat(utilisateur.getNom()).isEqualTo(utilisateurPostDTO.nom());
        assertThat(utilisateur.getSiret()).isEqualTo(utilisateurPostDTO.siret());
        assertThat(utilisateur.getCodePostal()).isEqualTo(utilisateurPostDTO.codePostal());
        assertThat(utilisateur.getTelephone()).isEqualTo(utilisateurPostDTO.telephone());
        assertThat(utilisateur.getSite()).isEqualTo(utilisateurPostDTO.site());
        assertThat(utilisateur.isRgpd()).isEqualTo(utilisateurPostDTO.rgpd());
        assertThat(utilisateur.getEmail()).isEqualTo(utilisateurPostDTO.email());
        assertThat(utilisateur.getMotDePasse()).isEqualTo(utilisateurPostDTO.motDePasse());

    }

    @Test
    void utilisateurToUtilisateurDTO() {
        //Given
        Produit produit = new Produit(UUID.fromString("e59ed17d-db7d-4d24-af6c-5154b3f72df0"), "Titre", "Petite description", new BigDecimal("10.27"), 8, new Categorie(), new Utilisateur());
        List<Produit> produits = new ArrayList<>();
        produits.add(produit);
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "https://test.com", UUID.randomUUID().toString(), true, true,"test@test.com", "1234AZER", null, produits);
        //When
        UtilisateurDTO utilisateurDTO = underTest.utilisateurToUtilisateurDTO(utilisateur);
        //Then
        assertThat(utilisateurDTO.nom()).isEqualTo(utilisateur.getNom());
        assertThat(utilisateurDTO.siret()).isEqualTo(utilisateur.getSiret());
        assertThat(utilisateurDTO.codePostal()).isEqualTo(utilisateur.getCodePostal());
        assertThat(utilisateurDTO.telephone()).isEqualTo(utilisateur.getTelephone());
        assertThat(utilisateurDTO.site()).isEqualTo(utilisateur.getSite());
        assertThat(utilisateurDTO.logo()).isEqualTo(utilisateur.getLogo());
        assertThat(utilisateurDTO.rgpd()).isEqualTo(utilisateur.isRgpd());
        assertThat(utilisateurDTO.email()).isEqualTo(utilisateur.getEmail());
        assertThat(utilisateurDTO.produits()).isNotNull();
    }

    @Test
    void produitToProduitUtilisateurDTO() {
        //Given
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>());
        Produit produit = new Produit(UUID.fromString("e59ed17d-db7d-4d24-af6c-5154b3f72df0"), "Titre", "Petite description", new BigDecimal("10.27"), 8, categorie, new Utilisateur());
        //When
        ProduitUtilisateurDTO produitUtilisateurDTO = underTest.produitToProduitUtilisateurDTO(produit);
        //Then
        assertThat(produitUtilisateurDTO.id()).isEqualTo(produit.getId());
        assertThat(produitUtilisateurDTO.titre()).isEqualTo(produit.getTitre());
        assertThat(produitUtilisateurDTO.description()).isEqualTo(produit.getDescription());
        assertThat(produitUtilisateurDTO.prix()).isEqualTo(produit.getPrix().doubleValue());
        assertThat(produitUtilisateurDTO.quantite()).isEqualTo(produit.getQuantite());
        assertThat(produitUtilisateurDTO.categorie()).isEqualTo(produit.getCategorie().getNom());
        assertThat(produitUtilisateurDTO.createdAt()).isEqualTo(produit.getCreatedAt());
        assertThat(produitUtilisateurDTO.updatedAt()).isEqualTo(produit.getUpdatedAt());
    }


}