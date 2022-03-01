package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.exception.NotFoundException;
import com.julianhusson.okastock.utils.GenericUtil;
import com.julianhusson.okastock.utils.Role;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public record UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Aucun utilisateur n'existe avec l'email " + email + "."));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        return new User(utilisateur.getEmail(), utilisateur.getMotDePasse(), authorities);
    }

    public Utilisateur getById(UUID id){
        return utilisateurRepository.findById(id).orElseThrow(() -> new NotFoundException("Aucun utilisateur n'existe avec l'id " + id + "."));
    }

    public void register(Utilisateur utilisateur){
        GenericUtil.checkUtilisateur(utilisateur);
        this.checkIfUnique(utilisateur.getSiret(), utilisateur.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        utilisateurRepository.save(utilisateur);
    }

    public void update(Utilisateur utilisateur){
        GenericUtil.checkUtilisateur(utilisateur);
        this.checkIfUnique(utilisateur.getSiret(), utilisateur.getEmail());
        Utilisateur utilisateurToUpdate = this.getById(utilisateur.getId());
        utilisateur.setMotDePasse(utilisateurToUpdate.getMotDePasse());
        utilisateurRepository.save(utilisateur);
    }

    public void delete(UUID userId){
        this.getById(userId);
        utilisateurRepository.deleteById(userId);
    }

    private void checkIfUnique(Long siret, String email){
        if(utilisateurRepository.existsBySiret(siret)) throw new DuplicateKeyException("Il existe déjà un compte avec ce SIRET.");
        if(utilisateurRepository.existsByEmail(email)) throw new DuplicateKeyException("Il existe déjà un compte avec cet email.");
    }
}
