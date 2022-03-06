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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public record AuthController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {

    @PostMapping("register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UtilisateurPostDTO utilisateurPostDTO, HttpServletRequest request){
        Map<String, String> tokens = this.utilisateurService.register(utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO), request.getRequestURL().toString());
        return new ResponseEntity<>(tokens, HttpStatus.CREATED);
    }
}
