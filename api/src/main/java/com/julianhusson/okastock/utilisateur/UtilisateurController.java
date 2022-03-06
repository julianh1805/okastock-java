package com.julianhusson.okastock.utilisateur;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.mapstruct.mapper.UtilisateurMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody UtilisateurPostDTO utilisateurPostDTO, @PathVariable UUID userId, HttpServletRequest request){
        Utilisateur utilisateur = utilisateurMapper.utilisateurPostDTOToUtilisateur(utilisateurPostDTO);
        utilisateur.setId(userId);
        Map<String, String> tokens = utilisateurService.update(utilisateur, request.getRequestURL().toString());
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity deleteUser(@PathVariable UUID userId){
        utilisateurService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
