package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.utils.GenericUtil;
import com.julianhusson.okastock.utils.Role;
import com.julianhusson.okastock.utils.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.GrantedAuthority;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur n'existe avec l'email " + email + "."));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        return new User(utilisateur.getEmail(), utilisateur.getMotDePasse(), authorities);
    }

    public Utilisateur getById(UUID id) {
        return utilisateurRepository.findById(id).orElseThrow(() -> new NotFoundException("Aucun utilisateur n'existe avec l'id " + id + "."));
    }

    public Map<String, String> register(Utilisateur utilisateur, String issuer) {
        GenericUtil.checkUtilisateur(utilisateur);
        this.isSiretUnique(utilisateur.getSiret());
        this.isEmailUnique(utilisateur.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurRepository.save(utilisateur);
        return TokenGenerator.generateTokens(utilisateur.getEmail(), null, issuer);
    }

    public Map<String, String> update(Utilisateur utilisateur, String issuer) {
        Utilisateur utilisateurToUpdate = this.getById(utilisateur.getId());
        GenericUtil.checkUtilisateur(utilisateur);
        if (!utilisateur.getEmail().equals(utilisateurToUpdate.getEmail())) this.isEmailUnique(utilisateur.getEmail());
        if (!utilisateur.getSiret().equals(utilisateurToUpdate.getSiret())) this.isSiretUnique(utilisateur.getSiret());
        utilisateur.setMotDePasse(utilisateurToUpdate.getMotDePasse());
        utilisateurRepository.save(utilisateur);
        return TokenGenerator.generateTokens(utilisateur.getEmail(), null, issuer);
    }

    public void delete(UUID userId) {
        this.getById(userId);
        utilisateurRepository.deleteById(userId);
    }

    private void isSiretUnique(Long siret) {
        if (utilisateurRepository.existsBySiret(siret))
            throw new DuplicateKeyException("Il existe déjà un compte avec ce SIRET.");
    }

    private void isEmailUnique(String email) {
        if (utilisateurRepository.existsByEmail(email))
            throw new DuplicateKeyException("Il existe déjà un compte avec cet email.");
    }

}
