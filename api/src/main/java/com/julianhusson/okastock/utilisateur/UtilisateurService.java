package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.email.EmailService;
import com.julianhusson.okastock.exception.ApiRequestException;
import com.julianhusson.okastock.exception.InvalidRegexException;
import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.role.Role;
import com.julianhusson.okastock.role.RoleRepository;
import com.julianhusson.okastock.storage.StorageService;
import com.julianhusson.okastock.utilisateur.validation.ValidationService;
import com.julianhusson.okastock.utilisateur.validation.ValidationToken;
import com.julianhusson.okastock.utils.TokenGenerator;
import com.julianhusson.okastock.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
    private final ValidationService validationService;
    private final StorageService storageService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur n'existe avec l'email " + email + "."));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        utilisateur.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getNom())));
        return new User(utilisateur.getId().toString(), utilisateur.getMotDePasse(), authorities);
    }

    public Utilisateur getById(UUID id) {
        return utilisateurRepository.findById(id).orElseThrow(() -> new NotFoundException("Aucun utilisateur n'existe avec l'id " + id + "."));
    }

    @Transactional
    public Map<String, String> register(Utilisateur utilisateur, MultipartFile logo, String issuer) {
        this.checkUtilisateur(utilisateur, logo, true);
        this.isSiretUnique(utilisateur.getSiret());
        this.isEmailUnique(utilisateur.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.getRoles().add(addRole("ROLE_USER"));
        utilisateur.setLogo(storageService.upsertLogo(logo, Optional.empty()));
        utilisateurRepository.save(utilisateur);
        ValidationToken validationToken = new ValidationToken(utilisateur);
        validationService.createValidationToken(validationToken);
        emailService.send(utilisateur.getEmail(), utilisateur.getNom(),  validationToken.getToken());
        return tokenGenerator.generateTokens(utilisateur.getId().toString(), utilisateur.getRoles().stream().map(Role::getNom).toList(), issuer);
    }

    public Utilisateur update(Utilisateur utilisateur, MultipartFile logo) {
        Utilisateur utilisateurToUpdate = this.getById(utilisateur.getId());
        Utils.checkAuthUser(utilisateurToUpdate.getId());
        this.checkUtilisateur(utilisateur, logo, false);
        if (!utilisateur.getEmail().equals(utilisateurToUpdate.getEmail())) this.isEmailUnique(utilisateur.getEmail());
        if (!utilisateur.getSiret().equals(utilisateurToUpdate.getSiret())) this.isSiretUnique(utilisateur.getSiret());
        utilisateur.setMotDePasse(utilisateurToUpdate.getMotDePasse());
        utilisateur.setCreatedAt(utilisateurToUpdate.getCreatedAt());
        utilisateur.setLogo(utilisateurToUpdate.getLogo());
        if(!logo.isEmpty()){
            storageService.upsertLogo(logo, Optional.ofNullable(utilisateurToUpdate.getLogo()));
        }
        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void delete(UUID utilisateurId) {
        Utilisateur utilisateur = this.getById(utilisateurId);
        Utils.checkAuthUser(utilisateurId);
        storageService.deleteLogo(utilisateur.getLogo());
        validationService.deleteAllByUtilisateurId(utilisateurId);
        utilisateurRepository.deleteById(utilisateurId);
    }

    @Transactional
    public void validate(String token) {
        UUID utilisateurId = validationService.confirmToken(token);
        Utilisateur utilisateur = getById(utilisateurId);
        utilisateur.setValid(true);
        utilisateurRepository.save(utilisateur);
    }

    private Role addRole(String role) {
        return roleRepository.findByNom(role).orElseThrow(() -> new NotFoundException("Aucun role " + role + " n'existe."));
    }

    private void isSiretUnique(Long siret) {
        if (utilisateurRepository.existsBySiret(siret))
            throw new DuplicateKeyException("Il existe d??j?? un compte avec ce SIRET.");
    }

    private void isEmailUnique(String email) {
        if (utilisateurRepository.existsByEmail(email))
            throw new DuplicateKeyException("Il existe d??j?? un compte avec cet email.");
    }

    private void checkUtilisateur(Utilisateur utilisateur, MultipartFile logo, boolean register){
        if(logo.isEmpty() && register) throw new ApiRequestException("Un logo est obligatoire pour cr??er un utilisateur.");
        if(utilisateur.getSiret().toString().length() != 14) throw new InvalidRegexException("Le SIRET doit faire 14 caracteres.");
        if(String.valueOf(utilisateur.getCodePostal()).length() != 5) throw new InvalidRegexException("Le code postal doit faire 5 caract??res.");
        String telephone = String.valueOf(utilisateur.getTelephone());
        if(telephone.length() != 9 || !telephone.startsWith("6") && !telephone.startsWith("7")) throw new InvalidRegexException("Le t??l??phone doit faire 9 chiffres et commencer par 6 ou 7.");
    }

}
