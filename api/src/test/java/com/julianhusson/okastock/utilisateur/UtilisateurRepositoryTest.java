package com.julianhusson.okastock.utilisateur;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/utilisateur-data.sql")
class UtilisateurRepositoryTest {

    @Autowired
    private UtilisateurRepository underTest;

    @Test
    void itShouldFindByEmail() {
        //Given
        String email = "test@test.com";
        //When
        Optional<Utilisateur> utilisateur = underTest.findByEmail(email);
        //Then
        assertThat(utilisateur).isPresent();
        assertThat(utilisateur.get().getEmail()).isEqualTo(email);
    }

    @Test
    void itShouldNotFindByEmail() {
        //Given
        String email = "tost@tost.com";
        //When
        Optional<Utilisateur> utilisateur = underTest.findByEmail(email);
        //Then
        assertThat(utilisateur).isEmpty();
    }

    @Test
    void itShouldExistBySiret() {
        //Given
        Long siret = 12345678910111L;
        //When
        boolean exists = underTest.existsBySiret(siret);
        //Then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldNotExistBySiret() {
        //Given
        Long siret = 12345678910112L;
        //When
        boolean exists = underTest.existsBySiret(siret);
        //Then
        assertThat(exists).isFalse();
    }

    @Test
    void itShouldExistByEmail() {
        //Given
        String email = "test@test.com";
        //When
        boolean exists = underTest.existsByEmail(email);
        //Then
        assertThat(exists).isTrue();
    }

    @Test
    void itShouldNotExistByEmail() {
        //Given
        String email = "tost@tost.com";
        //When
        boolean exists = underTest.existsByEmail(email);
        //Then
        assertThat(exists).isFalse();
    }
}