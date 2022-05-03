package com.julianhusson.okastock.security;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public record AuthController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {

    @PostMapping("register")
    public ResponseEntity<Map<String, String>> register(@ModelAttribute UtilisateurPostDTO utilisateurPostDTO, @RequestPart("logo") MultipartFile logo, HttpServletRequest request){
        Map<String, String> tokens = this.utilisateurService.register(utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO), logo, request.getRequestURL().toString());
        return new ResponseEntity<>(tokens, HttpStatus.CREATED);
    }

    @GetMapping("confirm")
    public ResponseEntity confirm(@RequestParam String token){
        this.utilisateurService.validate(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
