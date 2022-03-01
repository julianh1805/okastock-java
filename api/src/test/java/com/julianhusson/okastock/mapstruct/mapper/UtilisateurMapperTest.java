package com.julianhusson.okastock.mapstruct.mapper;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

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
        UtilisateurPostDTO utilisateurPostDTO = new UtilisateurPostDTO("Test", 12345678910111L, 44300, 666666666L, "https://test.com", "-", true, "test@test.com", "1234AZER");
        //When
        Utilisateur utilisateur = underTest.utilisateurPostDTOToUtilisateur(utilisateurPostDTO);
        //Then
        assertThat(utilisateur.getNom()).isEqualTo(utilisateurPostDTO.nom());
        assertThat(utilisateur.getSiret()).isEqualTo(utilisateurPostDTO.siret());
        assertThat(utilisateur.getCodePostal()).isEqualTo(utilisateurPostDTO.codePostal());
        assertThat(utilisateur.getTelephone()).isEqualTo(utilisateurPostDTO.telephone());
        assertThat(utilisateur.getSite()).isEqualTo(utilisateurPostDTO.site());
        assertThat(utilisateur.getLogo()).isEqualTo(utilisateurPostDTO.logo());
        assertThat(utilisateur.isRgpd()).isEqualTo(utilisateurPostDTO.rgpd());
        assertThat(utilisateur.getEmail()).isEqualTo(utilisateurPostDTO.email());
        assertThat(utilisateur.getMotDePasse()).isEqualTo(utilisateurPostDTO.motDePasse());

    }

    @Test
    void utilisateurToUtilisateurDTO() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "https://test.com", "-", true, "test@test.com", "1234AZER");
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
    }
}