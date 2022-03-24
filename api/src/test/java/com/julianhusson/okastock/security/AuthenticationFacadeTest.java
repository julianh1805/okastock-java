package com.julianhusson.okastock.security;

import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AuthenticationFacadeTest {

    @InjectMocks
    private AuthenticationFacade underTest;
    @Mock
    private UtilisateurService utilisateurService;
    private Authentication authentication;
    private SecurityContext securityContext;

    @BeforeEach
    void setUp(){
        authentication = Mockito.mock(Authentication.class);
        securityContext = Mockito.mock(SecurityContext.class);
    }

    @Test
    void itShouldGetAuthenticatedUser() {
        ///Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        Utilisateur utilisateur = new Utilisateur(utilisateurId, "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, "test@test.com", "1234AZER", null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new User("e59ed17d-db7c-4d24-af6c-5154b3f72dfe", "password", new ArrayList<>()));
        given(utilisateurService.getById(utilisateurId)).willReturn(utilisateur);
        ///When
        Utilisateur utilisateurRegistered = underTest.getAuthenticatedUser();
        ///Then
        assertThat(utilisateurRegistered).isNotNull();
    }

    @Test
    void itShouldThrowExceptionIfIdDoesNotExistsWhenGetAuthenticatedUser() {
        ///Given
        UUID badUtilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(new User(badUtilisateurId.toString(), "password", new ArrayList<>()));
        given(utilisateurService.getById(badUtilisateurId)).willThrow(
                new NotFoundException("Aucun utilisateur n'existe avec l'id " + badUtilisateurId + "."));        ///Then
        assertThatThrownBy(() -> underTest.getAuthenticatedUser())
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucun utilisateur n'existe avec l'id " + badUtilisateurId + ".");
    }
}
