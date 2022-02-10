package com.julianhusson.okastock.categorie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Sql("/categorie-data.sql")
class CategorieRepositoryTest {

    @Autowired
    private CategorieRepository underTest;

    @Test
    void itShouldFindByNom() {
        //Given
        String categorieName = "meubles";
        //When
        Optional<Categorie> categorie = underTest.findByNom(categorieName);
        //Then
        assertThat(categorie).isPresent();
        assertThat(categorie.get().getNom()).isEqualTo(categorieName);
    }

    @Test
    void itShouldNotFindByNom() {
        //Given
        String otherCategorieName = "meuble";
        //When
        Optional<Categorie> categorie = underTest.findByNom(otherCategorieName);
        //Then
        assertThat(categorie).isEmpty();
    }
}