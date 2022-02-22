package com.julianhusson.okastock.mapstruct.dto;

public record UtilisateurDTO(String nom, Long siret, int codePostal, Long telephone, String site, String logo, boolean rgpd, String email) {}
