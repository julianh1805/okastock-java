package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public record UtilisateurController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {

    @GetMapping("{userId}")
    public ResponseEntity<UtilisateurDTO> getUser(@PathVariable UUID userId){
        return new ResponseEntity<>(utilisateurMapper.utilisateurToUtilisateurDTO(utilisateurService.getById(userId)),
                HttpStatus.OK);
    }

    @PutMapping("{userId}")
    public ResponseEntity<UtilisateurDTO> updateUser(@ModelAttribute UtilisateurPostDTO utilisateurPostDTO, @RequestPart(required = false) MultipartFile logo, @PathVariable UUID userId){
        Utilisateur utilisateur = utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO);
        utilisateur.setId(userId);
        return new ResponseEntity<>(utilisateurMapper.utilisateurToUtilisateurDTO(utilisateurService.update(utilisateur, logo)), HttpStatus.OK);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity deleteUser(@PathVariable UUID userId){
        utilisateurService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
