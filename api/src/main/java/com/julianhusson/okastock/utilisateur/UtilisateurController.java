package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/users")
@RestController
public record UtilisateurController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {
}
