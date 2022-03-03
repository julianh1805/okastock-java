package com.julianhusson.okastock.mapstruct.dto;

import java.util.UUID;

public record UtilisateurDTO(UUID id, String nom, Long siret, int codePostal, Long telephone, String site, String logo, boolean rgpd, String email) {}
