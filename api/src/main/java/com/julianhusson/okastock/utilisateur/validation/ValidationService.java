package com.julianhusson.okastock.utilisateur.validation;

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
                new NotFoundException("Aucun token n'existe avec cet id."));
        if(validationToken.getConfirmedAt() != null){
            throw new ApiRequestException("Ce compte est déjà validé.");
        }
        if(validationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new ApiRequestException("Le token a expiré.");
        }
        validationToken.setConfirmedAt(LocalDateTime.now());
        validationRepository.save(validationToken);
        return validationToken.getUtilisateur().getId();
    }
}
