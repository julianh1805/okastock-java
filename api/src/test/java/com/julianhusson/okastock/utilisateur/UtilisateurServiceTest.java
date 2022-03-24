package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.exception.InvalidRegexException;
import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.role.Role;
import com.julianhusson.okastock.role.RoleRepository;
import com.julianhusson.okastock.utils.TokenGenerator;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class UtilisateurServiceTest {

    @InjectMocks
    private UtilisateurService underTest;
    @Mock private UtilisateurRepository utilisateurRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private TokenGenerator tokenGenerator;
    @Autowired
    private Validator validator;
    private final String registerIssuer = "test/api/v1/auth/register";
    private final String updateIssuer = "test/api/v1/auth/update";
    private final Role userRole = new Role(UUID.fromString("e59ed17d-db7c-4d24-af3c-5154b3f72dff"), "ROLE_USER");

    @Before
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void itShouldLoadUserByUsername() {
        //Given
        String email = "test@test.com";
        String motDePasse = "1234AZER";
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        given(utilisateurRepository.findByEmail(email)).willReturn(
                Optional.of(new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "https://test.com", "-", true, email, motDePasse, roles)));

        //When
        UserDetails userDetails = underTest.loadUserByUsername(email);
        //Then
        assertThat(userDetails.getUsername()).isEqualTo("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        assertThat(userDetails.getPassword()).isEqualTo(motDePasse);
        assertThat(userDetails.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    void itShouldThrownAnExeceptionIfNotFindByEmail() {
        //Given
        String email = "tes@test.com";
        given(utilisateurRepository.findByEmail(email)).willReturn(Optional.empty());
        //When
        assertThatThrownBy(() -> underTest.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Aucun utilisateur n'existe avec l'email " + email + ".");
    }

    @Test
    void itShouldGetById() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        given(utilisateurRepository.findById(utilisateurId)).willReturn(
                Optional.of(new Utilisateur(utilisateurId, "Test", 12345678910111L, 44300, 666666666L, "https://test.com", "-", true, "test@test.com", "1234AZER", null)));
        //When
        Utilisateur utilisateur = underTest.getById(utilisateurId);
        //Then
        assertThat(utilisateur.getId()).isEqualTo(utilisateurId);
    }

    @Test
    void itShouldThrownAnExeceptionIfNotGetById() {
        //Given
        UUID fakeUtilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4");
        given(utilisateurRepository.findById(fakeUtilisateurId)).willReturn(Optional.empty());
        //When
        assertThatThrownBy(() -> underTest.getById(fakeUtilisateurId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucun utilisateur n'existe avec l'id " + fakeUtilisateurId + ".");
    }

    @Test
    void itShouldThrowExceptionIfNomIsEmptyWhenSave() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), null, 12345678910111L, 44300, 666666666L, "http://www.siteweb.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        this.assertViolation(violations, "Un nom d'entreprise est obligatoire pour s'inscrire.");
    }

    @Test
    void itShouldThrowExceptionIfNomIsLessThan3LengthWhenSave() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), "Te", 12345678910111L, 44300, 666666666L, "http://www.siteweb.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        this.assertViolation(violations, "Le nom d'entreprise doit faire entre 3 et 30 caractères.");
    }

    @Test
    void itShouldThrowExceptionIfNomIsMoreThan30LengthWhenSave() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), "Test test test test test test test tes", 12345678910111L, 44300, 666666666L, "http://www.siteweb.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        this.assertViolation(violations, "Le nom d'entreprise doit faire entre 3 et 30 caractères.");
    }

    @Test
    void itShouldThrowExceptionIfSiretIsNullWhenSave() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), "Test", null, 44300, 666666666L, "http://www.siteweb.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        this.assertViolation(violations, "Un SIRET est obligatoire pour s'inscrire.");
    }

    @Test
    void itShouldThrowExceptionIfTelephoneIsNullWhenSave() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), "Test", 12345678910111L, 44300, null, "http://www.siteweb.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        this.assertViolation(violations, "Un telephone est obligatoire pour s'inscrire.");
    }

    @Test
    void itShouldValidateWebsiteRegex1WhenSave() {
        //Given
        String website = "http://website.com";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, website, "-", true, "test@test.com", "1234AZER", new ArrayList<>());
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        given(roleRepository.findByNom("ROLE_USER")).willReturn(Optional.of(userRole));
        //When
        underTest.register(utilisateurToAdd, registerIssuer);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToAdd);
    }

    @Test
    void itShouldValidateWebsiteRegex2WhenSave() {
        //Given
        String website = "https://website.com";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, website, "-", true, "test@test.com", "1234AZER", new ArrayList<>());
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        given(roleRepository.findByNom("ROLE_USER")).willReturn(Optional.of(userRole));
        //When
        underTest.register(utilisateurToAdd, registerIssuer);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToAdd);
    }

    @Test
    void itShouldValidateWebsiteRegex3WhenSave() {
        //Given
        String website = "https://www.website.com";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, website, "-", true, "test@test.com", "1234AZER", new ArrayList<>());
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        given(roleRepository.findByNom("ROLE_USER")).willReturn(Optional.of(userRole));
        //When
        underTest.register(utilisateurToAdd, registerIssuer);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToAdd);
    }

    @Test
    void itShouldNotValidateWebsiteRegex4WhenSave() {
        //Given
        String website = "htt://www.website.com";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, website, "-", true, "test@test.com", "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateurToAdd);
         assertViolation(violations, "Le site web est invalide. Le format devrait être http://(www).siteweb.com");
    }

    @Test
    void itShouldNotValidateWebsiteRegex5WhenSave() {
        //Given
        String website = "http://website";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, website, "-", true, "test@test.com", "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateurToAdd);
        assertViolation(violations, "Le site web est invalide. Le format devrait être http://(www).siteweb.com");
    }

    @Test
    void itShouldThrowExceptionIfEmailIsNullWhenSave() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), "Test", 12345678910111L, 44300, 666666666L, "http://www.siteweb.com", "-", true, null, "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        this.assertViolation(violations, "Un email est obligatoire pour s'inscrire.");
    }

    @Test
    void itShouldValidateEmailRegex1WhenSave() {
        //Given
        String email = "test@test.com";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", new ArrayList<>());
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        given(roleRepository.findByNom("ROLE_USER")).willReturn(Optional.of(userRole));
        //When
        underTest.register(utilisateurToAdd, registerIssuer);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToAdd);
    }

    @Test
    void itShouldValidateEmailRegex2WhenSave() {
        //Given
        String email = "test.test@test.com";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", new ArrayList<>());
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        given(roleRepository.findByNom("ROLE_USER")).willReturn(Optional.of(userRole));
        //When
        underTest.register(utilisateurToAdd, registerIssuer);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToAdd);
    }

    @Test
    void itShouldValidateEmailRegex3WhenSave() {
        //Given
        String email = "test@test";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", new ArrayList<>());
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        given(roleRepository.findByNom("ROLE_USER")).willReturn(Optional.of(userRole));
        //When
        underTest.register(utilisateurToAdd, registerIssuer);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToAdd);
    }

    @Test
    void itShouldNotValidateEmailRegex4WhenSave() {
        //Given
        String email = ".test@test";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateurToAdd);
        assertViolation(violations, "L'email est invalide");
    }

    @Test
    void itShouldNotValidateEmailRegex5WhenSave() {
        //Given
        String email = "test#test@test";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateurToAdd);
        assertViolation(violations, "L'email est invalide");
    }

    @Test
    void itShouldNotValidateEmailRegex6WhenSave() {
        //Given
        String email = "test@test.";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateurToAdd);
        assertViolation(violations, "L'email est invalide");
    }

    @Test
    void itShouldNotValidateEmailRegex7WhenSave() {
        //Given
        String email = "test.@test";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateurToAdd);
        assertViolation(violations, "L'email est invalide");
    }

    @Test
    void itShouldNotValidateEmailRegex8WhenSave() {
        //Given
        String email = "test@.test";
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.website.com", "-", true, email, "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateurToAdd);
        assertViolation(violations, "L'email est invalide");
    }

    @Test
    void itShouldRegister() {
        //Given
        Utilisateur utilisateurToAdd = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44300, 666666666L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", new ArrayList<>());
        given(passwordEncoder.encode(utilisateurToAdd.getMotDePasse())).willReturn("1234AZER");
        given(roleRepository.findByNom("ROLE_USER")).willReturn(Optional.of(userRole));
        //When
        underTest.register(utilisateurToAdd, registerIssuer);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToAdd);
    }

    @Test
    void itShouldThrowExceptionIfSiretLenghIsNot14WhenRegister() {
        //Given
        Utilisateur utilisateur = new Utilisateur(null, "Test", 123456789101L, 44300, 666666666L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        assertThatThrownBy(() -> underTest.register(utilisateur, registerIssuer))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le SIRET doit faire 14 caracteres.");
    }

    @Test
    void itShouldThrowExceptionIfSiretAlreadyExistsWhenRegister() {
        //Given
        Long siret = 12345678910111L;
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72db9"), "Test", siret, 44400, 666666656L, "http://www.test.com", "-", true, "test2@test.com", "1234AZER", null);
        given(utilisateurRepository.existsBySiret(siret)).willReturn(true);
        //When
        assertThatThrownBy(() -> underTest.register(utilisateur, registerIssuer))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("Il existe déjà un compte avec ce SIRET.");
    }

    @Test
    void itShouldThrowExceptionIfEmailAlreadyExistsWhenRegister() {
        //Given
        String email = "test@test.com";
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        Utilisateur utilisateur = new Utilisateur(utilisateurId, "Test", 12345678910111L, 44400, 666666656L, "http://www.test.com", "-", true, email, "1234AZER", null);
        given(utilisateurRepository.findById(utilisateurId)).willReturn(
                Optional.of(utilisateur));
        given(utilisateurRepository.existsByEmail(email)).willReturn(true);
        //When
        assertThatThrownBy(() -> underTest.register(utilisateur, registerIssuer))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("Il existe déjà un compte avec cet email.");
    }

    @Test
    void itShouldThrowExceptionIfCodePostalLenghIsNot5WhenRegister() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72db9"), "Test", 12345678910111L, 443, 666666666L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        assertThatThrownBy(() -> underTest.register(utilisateur, registerIssuer))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le code postal doit faire 5 caractères.");
    }

    @Test
    void itShouldThrowExceptionIfTelephoneLengthIsNot9WhenRegister() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), "Test", 12345678910111L, 44300, 63356859L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertThatThrownBy(() -> underTest.register(utilisateur, registerIssuer))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le téléphone doit faire 9 chiffres et commencer par 6 ou 7.");
    }

    @Test
    void itShouldThrowExceptionIfTelephoneLengthItDoesntStartWith6Or7WhenRegister() {
        //Given
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4"), "Test", 12345678910111L, 44300, 843356859L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertThatThrownBy(() -> underTest.register(utilisateur, registerIssuer))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le téléphone doit faire 9 chiffres et commencer par 6 ou 7.");
    }

    @Test
    void itShouldUpdate() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        Utilisateur utilisateurToUpdate = new Utilisateur(utilisateurId, "Test", 12345678910111L, 44300, 666666666L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        given(passwordEncoder.encode(utilisateurToUpdate.getMotDePasse())).willReturn("1234AZER");
        given(utilisateurRepository.findById(utilisateurId)).willReturn(
                Optional.of(new Utilisateur(utilisateurId, "Test", 12345678910111L, 44300, 666666666L, "https://test.com", "-", true, "test@test.com", "1234AZER", null)));
        //When
        underTest.update(utilisateurToUpdate);
        //Then
        this.assertSave(ArgumentCaptor.forClass(Utilisateur.class), utilisateurToUpdate);

    }

    @Test
    void itShouldThrowExceptionIfSiretLenghIsNot14WhenUpdate() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        Utilisateur utilisateur = new Utilisateur(utilisateurId, "Test", 123456789101L, 44300, 666666666L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        given(utilisateurRepository.findById(utilisateurId)).willReturn(Optional.of(utilisateur));
        //When
        assertThatThrownBy(() -> underTest.update(utilisateur))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le SIRET doit faire 14 caracteres.");
    }

    @Test
    void itShouldThrowExceptionIfSiretAlreadyExistsWhenUpdate() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44400, 666666656L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        Utilisateur utilisateurToUpdate = new Utilisateur(utilisateurId, "Test", 12345678910112L, 44400, 666666656L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        given(utilisateurRepository.findById(utilisateurId)).willReturn(Optional.of(utilisateur));
        given(utilisateurRepository.existsBySiret(12345678910112L)).willReturn(true);
        //When
        assertThatThrownBy(() -> underTest.update(utilisateurToUpdate))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("Il existe déjà un compte avec ce SIRET.");
    }

    @Test
    void itShouldThrowExceptionIfEmailAlreadyExistsWhenUpdate() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe");
        Utilisateur utilisateur = new Utilisateur(UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72dfe"), "Test", 12345678910111L, 44400, 666666656L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        Utilisateur utilisateurToUpdate = new Utilisateur(utilisateurId, "Test", 12345678910112L, 44400, 666666656L, "http://www.test.com", "-", true, "test2@test.com", "1234AZER", null);
        given(utilisateurRepository.findById(utilisateurId)).willReturn(Optional.of(utilisateur));
        given(utilisateurRepository.existsByEmail("test2@test.com")).willReturn(true);
        //When
        assertThatThrownBy(() -> underTest.register(utilisateurToUpdate, registerIssuer))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessageContaining("Il existe déjà un compte avec cet email.");
    }

    @Test
    void itShouldThrowExceptionIfCodePostalLenghIsNot5WhenUpdate() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72db9");
        Utilisateur utilisateur = new Utilisateur(utilisateurId, "Test", 12345678910111L, 443, 666666666L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        given(utilisateurRepository.findById(utilisateurId)).willReturn(
                Optional.of(utilisateur));
        given(utilisateurRepository.existsByEmail("test@test.com")).willReturn(true);
        //When
        assertThatThrownBy(() -> underTest.update(utilisateur))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le code postal doit faire 5 caractères.");
    }

    @Test
    void itShouldThrowExceptionIfTelephoneLengthIsNot9WhenUpdate() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4");
        Utilisateur utilisateur = new Utilisateur(utilisateurId, "Test", 12345678910111L, 44300, 63356859L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        given(utilisateurRepository.findById(utilisateurId)).willReturn(Optional.of(utilisateur));
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertThatThrownBy(() -> underTest.update(utilisateur))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le téléphone doit faire 9 chiffres et commencer par 6 ou 7.");
    }

    @Test
    void itShouldThrowExceptionIfTelephoneLengthItDoesntStartWith6Or7WhenUpdate() {
        //Given
        UUID utilisateurId = UUID.fromString("e59ed17d-db7c-4d24-af6c-5154b3f72df4");
        Utilisateur utilisateur = new Utilisateur(utilisateurId, "Test", 12345678910111L, 44300, 843356859L, "http://www.test.com", "-", true, "test@test.com", "1234AZER", null);
        given(utilisateurRepository.findById(utilisateurId)).willReturn(Optional.of(utilisateur));
        //When
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        assertThatThrownBy(() -> underTest.update(utilisateur))
                .isInstanceOf(InvalidRegexException.class)
                .hasMessageContaining("Le téléphone doit faire 9 chiffres et commencer par 6 ou 7.");
    }

    @Test
    void itShouldDelete() {
        //Given
        UUID productId = UUID.randomUUID();
        given(utilisateurRepository.findById(productId)).willReturn(Optional.of(new Utilisateur()));
        //When
        underTest.delete(productId);
        //Then
        verify(utilisateurRepository).deleteById(productId);
    }

    @Test
    void itShouldThrownAnExeceptionIfIdNotExistsWhenDelete() {
        //Given
        UUID fakeProductId = UUID.randomUUID();
        given(utilisateurRepository.existsById(fakeProductId)).willReturn(false);
        //Then
        assertThatThrownBy(() -> underTest.delete(fakeProductId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Aucun utilisateur n'existe avec l'id " + fakeProductId + ".");
    }


    private void assertSave(ArgumentCaptor<Utilisateur> argumentCaptor, Utilisateur utilisateur){
        verify(utilisateurRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(utilisateur);
    }

    private void assertViolation(Set<ConstraintViolation<Utilisateur>> violations, String message){
        assertFalse(violations.isEmpty());
        assertThat(violations.stream().findFirst().get().getMessage()).isEqualTo(message);
    }
}