package com.julianhusson.okastock.utilisateur.validation;

import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Sql({"/utilisateur-data.sql", "/validation-token-data.sql"})
class ValidationRepositoryTest {

    @Autowired
    private ValidationRepository underTest;

    @Test
    void itShouldFindByToken() {
        //Given
        String token = "00b02bb5-0424-4251-8a23-d1030cb52754";
        //When
        Optional<ValidationToken> validation = underTest.findByToken(token);
        //Then
        assertThat(validation).isPresent();
        assertThat(validation.get().getToken()).isEqualTo(token);
        assertThat(validation.get().getUtilisateur().getId()).isEqualTo(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"));
    }

    @Test
    void itShouldNotFindByToken() {
        //Given
        String token = "00b02bb5-0424-4251-8a23-d1030cb52744";
        //When
        Optional<ValidationToken> validation = underTest.findByToken(UUID.randomUUID().toString());
        //Then
        assertThat(validation).isEmpty();
    }

    @Test
    void itShouldDeleteAllByUtilisateurId() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        //When
        underTest.deleteAllByUtilisateurId(utilisateurId);
        //Then
        assertThat(underTest.count()).isEqualTo(0);
    }
}