package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/v1/users")
@RestController
public record UtilisateurController(UtilisateurService utilisateurService, UtilisateurMapper utilisateurMapper) {

    @GetMapping("{userId}")
    public ResponseEntity<UtilisateurDTO> getUser(@PathVariable UUID userId){
        return new ResponseEntity<>(utilisateurMapper.utilisateurToUtilisateurDTO(utilisateurService.getById(userId)),
                HttpStatus.OK);
    }

    @PutMapping("{userId}")
    public ResponseEntity updateUser(@RequestBody UtilisateurPostDTO utilisateurPostDTO, @PathVariable UUID userId){
        Utilisateur utilisateur = utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO);
        utilisateur.setId(userId);
        utilisateurService.update(utilisateur);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity deleteUser(@PathVariable UUID userId){
        utilisateurService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}