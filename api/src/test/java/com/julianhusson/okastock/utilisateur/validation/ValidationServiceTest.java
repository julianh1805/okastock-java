package com.julianhusson.okastock.utilisateur.validation;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.julianhusson.okastock.exception.ApiRequestException;
import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class ValidationServiceTest {

    @InjectMocks private ValidationService underTest;
    @Mock private ValidationRepository validationRepository;
    private final Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 843356859L, "http://www.test.com", UUID.randomUUID().toString(), true,  false, "test@test.com", "1234AZER", null, new ArrayList<>());

    @Test
    void itShouldCreateValidationToken() {
        //Given
        ValidationToken validationToken = new ValidationToken(utilisateur);
        //When
        underTest.createValidationToken(validationToken);
        //Then
        ArgumentCaptor<ValidationToken> validationTokenArgumentCaptor = ArgumentCaptor.forClass(ValidationToken.class);
        verify(validationRepository).save(validationTokenArgumentCaptor.capture());
        assertThat(validationTokenArgumentCaptor.getValue()).isEqualTo(validationToken);
    }

    @Test
    void itShouldConfirmToken() {
        //Given
        ValidationToken validationToken = new ValidationToken(utilisateur);
        given(validationRepository.findByToken(validationToken.getToken())).willReturn(Optional.of(validationToken));
        //When
        UUID utilisateurTokenId = underTest.confirmToken(validationToken.getToken());
        //Then
        assertThat(utilisateurTokenId).isEqualTo(validationToken.getUtilisateur().getId());
    }

    @Test
    void itShouldThrownExceptionIfTokenDoesNotExistWhenConfirmToken() {
        //Given
        ValidationToken validationToken = new ValidationToken(utilisateur);
        given(validationRepository.findByToken(validationToken.getToken())).willReturn(Optional.empty());
        //When
        assertThatThrownBy(() -> underTest.confirmToken(validationToken.getToken()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Le lien est indisponible.");
    }

    @Test
    void itShouldThrownExceptionIfTokenAlreadyConfirmedWhenConfirmToken() {
        //Given
        ValidationToken validationToken = new ValidationToken(utilisateur);
        validationToken.getUtilisateur().setValid(true);
        given(validationRepository.findByToken(validationToken.getToken())).willReturn(Optional.of(validationToken));
        //When
        assertThatThrownBy(() -> underTest.confirmToken(validationToken.getToken()))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Ce compte est déjà vérifié.");
    }

    @Test
    void itShouldThrownExceptionIfTokenAlreadyExpiredWhenConfirmToken() {
        //Given
        ValidationToken validationToken = new ValidationToken(utilisateur);
        validationToken.setExpiresAt(LocalDateTime.now().minusMinutes(15));
        given(validationRepository.findByToken(validationToken.getToken())).willReturn(Optional.of(validationToken));
        //When
        assertThatThrownBy(() -> underTest.confirmToken(validationToken.getToken()))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("Ce lien a expiré.");
    }

    @Test
    void itShouldDeleteAllByUtilisateurId() {
        //Given
        ValidationToken validationToken = new ValidationToken(utilisateur);
        validationToken.getUtilisateur().setValid(true);
        //When
        underTest.deleteAllByUtilisateurId(utilisateur.getId());
        //Then
        verify(validationRepository).deleteAllByUtilisateurId(utilisateur.getId());

    }
}