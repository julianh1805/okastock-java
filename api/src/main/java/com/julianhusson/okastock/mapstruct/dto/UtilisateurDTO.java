package com.julianhusson.okastock.mapstruct.dto;

import java.util.List;
import java.util.UUID;

public record UtilisateurDTO(UUID id, String logo, String nom, Long siret, int codePostal, Long telephone, String site, boolean rgpd, String email, Long createdAt, Long updatedAt, List<ProduitUtilisateurDTO> produits) {}
