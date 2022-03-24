package com.julianhusson.okastock.security;

import com.julianhusson.okastock.utilisateur.Utilisateur;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public record AuthenticationFacade(UtilisateurService utilisateurService) {

    public Utilisateur getAuthenticatedUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return utilisateurService.getById(UUID.fromString(user.getUsername()));
    }
}
