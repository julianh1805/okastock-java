package com.julianhusson.okastock.utilisateur.validation;

import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Sql({"/utilisateur-data.sql", "/validation-token-data.sql"})
class ValidationRepositoryTest {

    @Autowired
    private ValidationRepository underTest;
    private final UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
    private final Utilisateur utilisateur = new Utilisateur(utilisateurId, "Test", 12345678910111L, 44300, 843356859L, "http://www.test.com", "-", true,  false, "test@test.com", "1234AZER", null, new ArrayList<>());

    @Test
    void itShouldFindByToken() {
        //Given
        String token = "00b02bb5-0424-4251-8a23-d1030cb52754";
        //When
        Optional<ValidationToken> validation = underTest.findByToken(token);
        //Then
        assertThat(validation).isPresent();
        assertThat(validation.get().getToken()).isEqualTo(token);
        assertThat(validation.get().getUtilisateur().getId()).isEqualTo(utilisateurId);
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
}