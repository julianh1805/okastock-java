package com.julianhusson.okastock.mapstruct.dto;

import java.util.Date;
import java.util.UUID;

public record ProduitDTO(UUID id, String titre, String description, double prix, int quantite, String categorie, Long createdAt, Long updatedAt) {}
