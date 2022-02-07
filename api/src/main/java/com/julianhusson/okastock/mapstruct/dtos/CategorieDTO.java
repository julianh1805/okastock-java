package com.julianhusson.okastock.mapstruct.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class CategorieDTO {
    private UUID id;
    private String nom;
    private Set<CategorieProduitDTO> produits;
}
