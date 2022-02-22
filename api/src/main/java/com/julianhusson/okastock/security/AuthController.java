package com.julianhusson.okastock.security;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/auth")
@RestController
public record AuthController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {

    @PostMapping("register")
    public ResponseEntity registerUser(@RequestBody UtilisateurPostDTO utilisateurPostDTO){
        this.utilisateurService.register(utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
