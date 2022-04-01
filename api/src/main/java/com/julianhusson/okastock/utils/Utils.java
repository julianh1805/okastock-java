package com.julianhusson.okastock.utils;

import com.julianhusson.okastock.exception.BadUserException;
import com.julianhusson.okastock.exception.InvalidRegexException;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;

@UtilityClass
public class Utils {
    public void checkAuthUser(UUID id){
        String userId = String.valueOf(id);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println(userId);
        if(!userId.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            throw new BadUserException("Vous n'avez pas les droits pour effectuer cette action.");
        }
    }
}
