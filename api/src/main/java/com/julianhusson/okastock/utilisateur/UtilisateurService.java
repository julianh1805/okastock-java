package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.exception.InvalidRegexException;
import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.role.Role;
import com.julianhusson.okastock.role.RoleRepository;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;

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

    public Map<String, String> register(Utilisateur utilisateur, String issuer) {
        this.checkUtilisateur(utilisateur);
        this.isSiretUnique(utilisateur.getSiret());
        this.isEmailUnique(utilisateur.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateur.getRoles().add(addRole("ROLE_USER"));
        utilisateurRepository.save(utilisateur);
        return tokenGenerator.generateTokens(utilisateur.getId().toString(), utilisateur.getRoles().stream().map(Role::getNom).toList(), issuer);
    }

    public Utilisateur update(Utilisateur utilisateur) {
        Utilisateur utilisateurToUpdate = this.getById(utilisateur.getId());
        Utils.checkAuthUser(utilisateurToUpdate.getId());
        this.checkUtilisateur(utilisateur);
        if (!utilisateur.getEmail().equals(utilisateurToUpdate.getEmail())) this.isEmailUnique(utilisateur.getEmail());
        if (!utilisateur.getSiret().equals(utilisateurToUpdate.getSiret())) this.isSiretUnique(utilisateur.getSiret());
        utilisateur.setMotDePasse(utilisateurToUpdate.getMotDePasse());
        utilisateur.setCreatedAt(utilisateurToUpdate.getCreatedAt());
        return utilisateurRepository.save(utilisateur);
    }

    public void delete(UUID userId) {
        this.getById(userId);
        Utils.checkAuthUser(userId);
        utilisateurRepository.deleteById(userId);
    }

    private Role addRole(String role) {
        return roleRepository.findByNom(role).orElseThrow(() -> new NotFoundException("Aucun role " + role + " n'existe."));
    }

    private void isSiretUnique(Long siret) {
        if (utilisateurRepository.existsBySiret(siret))
            throw new DuplicateKeyException("Il existe déjà un compte avec ce SIRET.");
    }

    private void isEmailUnique(String email) {
        if (utilisateurRepository.existsByEmail(email))
            throw new DuplicateKeyException("Il existe déjà un compte avec cet email.");
    }

    private void checkUtilisateur(Utilisateur utilisateur){
        if(utilisateur.getSiret().toString().length() != 14) throw new InvalidRegexException("Le SIRET doit faire 14 caracteres.");
        if(String.valueOf(utilisateur.getCodePostal()).length() != 5) throw new InvalidRegexException("Le code postal doit faire 5 caractères.");
        String telephone = String.valueOf(utilisateur.getTelephone());
        if(telephone.length() != 9 || !telephone.startsWith("6") && !telephone.startsWith("7")) throw new InvalidRegexException("Le téléphone doit faire 9 chiffres et commencer par 6 ou 7.");
    }

}
