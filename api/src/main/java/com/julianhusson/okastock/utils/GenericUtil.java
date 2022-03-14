package com.julianhusson.okastock.utils;

import com.julianhusson.okastock.exception.InvalidRegexException;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GenericUtil {

    public void checkUtilisateur(Utilisateur utilisateur){
        if(utilisateur.getSiret().toString().length() != 14) throw new InvalidRegexException("Le SIRET doit faire 14 caracteres.");
        if(String.valueOf(utilisateur.getCodePostal()).length() != 5) throw new InvalidRegexException("Le code postal doit faire 5 caractères.");
        String telephone = String.valueOf(utilisateur.getTelephone());
        if(telephone.length() != 9 || !telephone.startsWith("6") && !telephone.startsWith("7")) throw new InvalidRegexException("Le téléphone doit faire 9 chiffres et commencer par 6 ou 7.");
    }
}
