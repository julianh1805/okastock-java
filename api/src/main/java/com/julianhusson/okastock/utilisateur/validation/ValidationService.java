package com.julianhusson.okastock.utilisateur.validation;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.julianhusson.okastock.exception.ApiRequestException;
import com.julianhusson.okastock.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public record ValidationService(ValidationRepository validationRepository){

    public void createValidationToken(ValidationToken token){
        validationRepository.save(token);
    }

    public UUID confirmToken(String token) {
        ValidationToken validationToken = this.validationRepository.findByToken(token).orElseThrow(() ->
                new NotFoundException("Le lien est indisponible."));
        if(validationToken.getUtilisateur().isValid()){
            throw new ApiRequestException("Ce compte est déjà vérifié.");
        }
        if(validationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new TokenExpiredException("Ce lien a expiré.");
        }
        validationRepository.deleteAllByUtilisateurId(validationToken.getUtilisateur().getId());
        return validationToken.getUtilisateur().getId();
    }

    public void deleteAllByUtilisateurId(UUID utilisateurId){
        validationRepository.deleteAllByUtilisateurId(utilisateurId);
    }
}
