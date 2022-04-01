package com.julianhusson.okastock.mapstruct.dto;

import java.util.UUID;

public record UtilisateurProduitDTO(UUID id, String nom, Long siret, int codePostal, Long telephone, String site, String logo, boolean rgpd, String email, Long createdAt, Long updatedAt) {}
