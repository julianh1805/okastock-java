package com.julianhusson.okastock.categorie;

import com.julianhusson.okastock.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class CategorieServiceTest {

    @InjectMocks private CategorieService underTest;
    @Mock private CategorieRepository categorieRepository;

    @Test
    void itShouldFindByNom() {
        //Given
        String categorieName = "meubles";
        Categorie categorie = new Categorie(UUID.randomUUID(), categorieName, new HashSet<>());
        given(categorieRepository.findByNom(categorieName)).willReturn(Optional.of(categorie));
        //When
        Categorie categorie1 = underTest.findByNom(categorieName);
        //Then
        assertThat(categorie1.getNom()).isEqualTo(categorieName);
    }

    @Test
    void itShouldThrownExceptionIfNotFindByNom() {
        //Given
        String otherCategorieName = "voiture";
        given(categorieRepository.findByNom(otherCategorieName)).willReturn(Optional.empty());
        //Then
        assertThatThrownBy(() -> underTest.findByNom(otherCategorieName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("La categorie '" + otherCategorieName + "' n'existe pas");
    }
}