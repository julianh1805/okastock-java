package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.categorie.CategorieService;
import com.julianhusson.okastock.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    @InjectMocks private ProduitService underTest;
    @Mock private ProduitRepository produitRepository;
    @Mock private CategorieService categorieService;

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
                Optional.of(new Produit(produitId, "Titre", "Petite description", 10.27, 8,
                        new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles",new HashSet<>()))));
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
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>());
        Produit produitToAdd = new Produit(UUID.randomUUID(), null, "Petite description", 10.27, 8,
                categorie);
        //When
        underTest.add(produitToAdd, categorie.getNom());
        //Then
        ArgumentCaptor<Produit> produitArgumentCaptor =
                ArgumentCaptor.forClass(Produit.class);
        verify(produitRepository).save(produitArgumentCaptor.capture());
        assertThat(produitArgumentCaptor.getValue()).isEqualTo(produitToAdd);
    }

    @Test
    void itShouldThrowExceptionIfNotFindByCategorieWhenAdd() {
        //Given
        Categorie categorie = new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meuble moisi", new HashSet<>());
        Produit produitToAdd = new Produit(UUID.randomUUID(), null, "Petite description", 10.27, 8,
                categorie);
        given(categorieService.findByNom(produitToAdd.getCategorie().getNom())).willThrow(
                new NotFoundException("Aucune catégorie " + produitToAdd.getCategorie().getNom() + " n'existe."));
        //Then
        assertThatThrownBy(() -> underTest.add(produitToAdd, categorie.getNom()))
                .hasMessageContaining("Aucune catégorie " + produitToAdd.getCategorie().getNom() + " n'existe.")
                .isInstanceOf(NotFoundException.class);
        verify(produitRepository, never()).save(any());
    }

    @Test
    void itShouldUpdate() {
        //Given
        UUID productId = UUID.randomUUID();
        Produit produitToUpdate = new Produit(productId, null, "Petite description", 10.27, 8,
                new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>()));
        given(produitRepository.existsById(productId)).willReturn(true);
        //When
        underTest.update(produitToUpdate);
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
        Produit produitToUpdate = new Produit(fakeProduitId, null, "Petite description", 10.27, 8,
                new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meubles", new HashSet<>()));
        given(produitRepository.existsById(fakeProduitId)).willThrow(
                new NotFoundException("Aucun produit n'existe avec l'id " + fakeProduitId + "."));
        //Then
        assertThatThrownBy(() -> underTest.update(produitToUpdate))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucun produit n'existe avec l'id " + fakeProduitId + ".");
    }

    @Test
    void itShouldThrownAnExeceptionIfNotFindCategorieByNameWhenUpdate() {
        //Given
        UUID productId = UUID.randomUUID();
        Produit produitToUpdate = new Produit(productId, null, "Petite description", 10.27, 8,
                new Categorie(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df0"), "meuble moisi",new HashSet<>()));
        given(produitRepository.existsById(productId)).willReturn(true);
        given(categorieService.findByNom(produitToUpdate.getCategorie().getNom())).willThrow(
                new NotFoundException("Aucune catégorie " + produitToUpdate.getCategorie().getNom() + " n'existe."));
        //Then
        assertThatThrownBy(() -> underTest.update(produitToUpdate))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucune catégorie " + produitToUpdate.getCategorie().getNom() + " n'existe.");
    }

    @Test
    void itShouldDelete() {
        //Given
        UUID productId = UUID.randomUUID();
        given(produitRepository.existsById(productId)).willReturn(true);
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
