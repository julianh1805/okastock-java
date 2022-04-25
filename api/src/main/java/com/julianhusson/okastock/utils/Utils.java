package com.julianhusson.okastock.utils;

import com.julianhusson.okastock.exception.BadUserException;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

@UtilityClass
public class Utils {
    public void checkAuthUser(UUID id){
        String userId = String.valueOf(id);
        if(!userId.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())){
            throw new BadUserException("Vous n'avez pas les droits pour effectuer cette action.");
        }
    }
}
