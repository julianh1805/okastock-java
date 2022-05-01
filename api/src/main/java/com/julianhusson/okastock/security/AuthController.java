package com.julianhusson.okastock.security;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import com.julianhusson.okastock.storage.StorageService;
import com.julianhusson.okastock.utilisateur.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public record AuthController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper, StorageService storageService) {

    @PostMapping("register")
    public ResponseEntity<Map<String, String>> register(@ModelAttribute UtilisateurPostDTO utilisateurPostDTO, @RequestPart("profil") MultipartFile profil, HttpServletRequest request){
        //Map<String, String> tokens = this.utilisateurService.register(utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO), request.getRequestURL().toString());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("confirm")
    public ResponseEntity confirm(@RequestParam String token){
        this.utilisateurService.validate(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("image")
    public ResponseEntity getImage(){
        this.storageService.getProfilImage();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
