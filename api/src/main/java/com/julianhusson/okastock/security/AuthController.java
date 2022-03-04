package com.julianhusson.okastock.security;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public record AuthController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {

    @PostMapping("register")
    public ResponseEntity<UtilisateurDTO> register(@RequestBody UtilisateurPostDTO utilisateurPostDTO){
        Utilisateur utilisateur = this.utilisateurService.register(utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO));
        UtilisateurDTO utilisateurDTO = utilisateurMapper.utilisateurToUtilisateurDTO(utilisateur);
        return new ResponseEntity<>(utilisateurDTO, HttpStatus.CREATED);
    }
}
