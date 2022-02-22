package com.julianhusson.okastock.mapstruct.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtilisateurDTO extends AuditableDTO{
    private String nom;
    private Long siret;
    private int codePostal;
    private Long telephone;
    private String site;
    private String logo;
    private boolean rgpd;
    private String email;
}
