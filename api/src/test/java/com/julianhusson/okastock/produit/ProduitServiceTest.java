package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.categorie.CategorieService;
import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.security.AuthenticationFacade;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ProduitServiceTest {

    @InjectMocks private ProduitService underTest;
    @Mock private ProduitRepository produitRepository;
    @Mock private CategorieService categorieService;
    @Mock private AuthenticationFacade authenticationFacade;
    @Autowired private Validator validator;

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void itShouldGetAll(){
        //When
        underTest.getAll();
        //Then
        verify(produitRepository).findAll();
    }


    @Test
    void itShouldGetById() {
        //Given
        UUID produitId = UUID.fromString("e59ed17d-db7d-4d24-af6c-5154b3f72df0");
        given(produitRepository.findById(produitId)).willReturn(
                Optional.of(new Produit(produitId, "Titre", "Petite description", new BigDecimal("10.27"), 8,
                        new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dff"), "meubles",new HashSet<>()), new Utilisateur())));
        //When
        Produit produit = underTest.getById(produitId);
        //Then
        assertThat(produit.getId()).isEqualTo(produitId);
    }

    @Test
    void itShouldThrownAnExeceptionIfNotGetById() {
        //Given
        UUID fakeProduitId = UUID.randomUUID();
        given(produitRepository.findById(fakeProduitId)).willReturn(Optional.empty());
        //When
        assertThatThrownBy(() -> underTest.getById(fakeProduitId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucun produit n'existe avec l'id " + fakeProduitId + ".");
    }

    @Test
    void itShouldAdd() {
        //Given
        String categorieName = "meubles";
        Produit produitToAdd = new Produit(null, "Titre", "Petite description", new BigDecimal("10.27"), 8, null, new Utilisateur());
        //When
        underTest.create(produitToAdd, categorieName);
        //Then
        ArgumentCaptor<Produit> produitArgumentCaptor =
                ArgumentCaptor.forClass(Produit.class);
        verify(produitRepository).save(produitArgumentCaptor.capture());
        assertThat(produitArgumentCaptor.getValue()).isEqualTo(produitToAdd);
    }

    @Test
    void itShouldThrowExceptionIfTitreIsEmptyWhenAdd() {
        //Given
        String categorieName = "meubles";
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dff"), categorieName, new HashSet<>());
        Produit produitToUpSert = new Produit(null, "", "Petite description", new BigDecimal("10.27"), 8, null, new Utilisateur());
        produitToUpSert.setCategorie(categorie);
        Set<ConstraintViolation<Produit>> violations = validator.validate(produitToUpSert);
        assertFalse(violations.isEmpty());
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("Un titre est obligatoire pour créer un produit.");
    }

    @Test
    void itShouldThrowExceptionIfDescriptionIsEmptyWhenAdd() {
        //Given
        String categorieName = "meubles";
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dff"), categorieName, new HashSet<>());
        Produit produitToUpSert = new Produit(null, "Titre", "", new BigDecimal("10.27"), 8, null, new Utilisateur());
        produitToUpSert.setCategorie(categorie);
        Set<ConstraintViolation<Produit>> violations = validator.validate(produitToUpSert);
        assertFalse(violations.isEmpty());
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("Une description est obligatoire pour créer un produit.");
    }

    @Test
    void itShouldThrowExceptionIfPriceIsEmptyWhenAdd() {
        //Given
        String categorieName = "meubles";
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dff"), categorieName, new HashSet<>());
        Produit produitToUpSert = new Produit(null, "Titre", "Description", new BigDecimal("0.0"), 8, null, new Utilisateur());
        produitToUpSert.setCategorie(categorie);
        Set<ConstraintViolation<Produit>> violations = validator.validate(produitToUpSert);
        assertFalse(violations.isEmpty());
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("Le prix doit être égale ou supérieur à 1 centime.");
    }

    @Test
    void itShouldUpdate() {
        //Given
        UUID productId = UUID.randomUUID();
        String categorieName = "meubles";
        Produit produitToUpdate = new Produit(productId, "Titre", "Petite description", new BigDecimal("10.27"), 8,null, new Utilisateur());
        given(produitRepository.existsById(productId)).willReturn(true);
        //When
        underTest.update(produitToUpdate, categorieName);
        //Then
        ArgumentCaptor<Produit> produitArgumentCaptor =
                ArgumentCaptor.forClass(Produit.class);
        verify(produitRepository).save(produitArgumentCaptor.capture());
        assertThat(produitArgumentCaptor.getValue()).isEqualTo(produitToUpdate);
    }

    @Test
    void itShouldThrownAnExeceptionIfNotGetByIdWhenUpdate() {
        //Given
        UUID fakeProduitId = UUID.randomUUID();
        String categorieName = "meubles";
        Produit produitToUpdate = new Produit(fakeProduitId, "Titre", "Petite description", new BigDecimal("10.27"), 8, null, new Utilisateur());
        given(produitRepository.existsById(fakeProduitId)).willThrow(
                new NotFoundException("Aucun produit n'existe avec l'id " + fakeProduitId + "."));
        //Then
        assertThatThrownBy(() -> underTest.update(produitToUpdate, categorieName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucun produit n'existe avec l'id " + fakeProduitId + ".");
    }

    @Test
    void itShouldThrowExceptionIfNotFindByCategorieWhenUpdate() {
        //Given
        UUID productId = UUID.randomUUID();
        String categorieName = "meuble moisi";
        Produit produitToUpdate = new Produit(productId, "Titre", "Petite description", new BigDecimal("10.27"), 8, null, new Utilisateur());
        given(produitRepository.existsById(productId)).willReturn(true);
        given(categorieService.findByNom(categorieName)).willThrow(
                new NotFoundException("Aucune catégorie " + categorieName + " n'existe."));
        //Then
        assertThatThrownBy(() -> underTest.update(produitToUpdate, categorieName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucune catégorie " + categorieName + " n'existe.");
    }

    @Test
    void itShouldThrowExceptionIfTitreIsEmptyWhenUpdate() {
        //Given
        UUID productId = UUID.randomUUID();
        String categorieName = "meubles";
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dff"), categorieName, new HashSet<>());
        Produit produitToUpSert = new Produit(productId, "", "Petite description", new BigDecimal("10.27"), 8, null, new Utilisateur());
        produitToUpSert.setCategorie(categorie);
        Set<ConstraintViolation<Produit>> violations = validator.validate(produitToUpSert);
        assertFalse(violations.isEmpty());
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("Un titre est obligatoire pour créer un produit.");
    }

    @Test
    void itShouldThrowExceptionIfDescriptionIsEmptyWhenUpdate() {
        //Given
        UUID productId = UUID.randomUUID();
        String categorieName = "meubles";
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dff"), categorieName, new HashSet<>());
        Produit produitToUpSert = new Produit(productId, "Titre", "", new BigDecimal("10.27"), 8, null, new Utilisateur());
        produitToUpSert.setCategorie(categorie);
        Set<ConstraintViolation<Produit>> violations = validator.validate(produitToUpSert);
        assertFalse(violations.isEmpty());
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("Une description est obligatoire pour créer un produit.");
    }

    @Test
    void itShouldThrowExceptionIfPriceIsEmptyWhenUpdate() {
        //Given
        UUID productId = UUID.randomUUID();
        String categorieName = "meubles";
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dff"), categorieName, new HashSet<>());
        Produit produitToUpSert = new Produit(productId, "Titre", "Description", new BigDecimal("0.0"), 8, null, new Utilisateur());
        produitToUpSert.setCategorie(categorie);
        Set<ConstraintViolation<Produit>> violations = validator.validate(produitToUpSert);
        assertFalse(violations.isEmpty());
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo("Le prix doit être égale ou supérieur à 1 centime.");
    }

    @Test
    void itShouldDelete() {
        //Given
        UUID productId = UUID.randomUUID();
        given(produitRepository.findById(productId)).willReturn(Optional.of(new Produit()));
        //When
        underTest.delete(productId);
        //Then
        verify(produitRepository).deleteById(productId);
    }

    @Test
    void itShouldThrownAnExeceptionIfIdNotExistsWhenDelete() {
        //Given
        UUID fakeProductId = UUID.randomUUID();
        given(produitRepository.existsById(fakeProductId)).willReturn(false);
        //Then
        assertThatThrownBy(() -> underTest.delete(fakeProductId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucun produit n'existe avec l'id " + fakeProductId + ".");
    }
}
