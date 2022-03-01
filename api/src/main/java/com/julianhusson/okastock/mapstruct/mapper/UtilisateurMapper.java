package com.julianhusson.okastock.mapstruct.mapper;

import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.mapstruct.Mapper;

@Mapper
public interface UtilisateurMapper {
    Utilisateur utilisateurPostDTOToUtilisateur(UtilisateurPostDTO utilisateurPostDTO);

    UtilisateurDTO utilisateurToUtilisateurDTO(Utilisateur utilisateur);

}
