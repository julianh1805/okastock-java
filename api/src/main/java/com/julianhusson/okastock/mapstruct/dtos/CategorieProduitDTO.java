package com.julianhusson.okastock.mapstruct.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CategorieProduitDTO {
    private UUID id;
    private String titre;
    private String description;
    private double prix;
    private int quantite;
    private Date createdAt;
    private Date updatedAt;
}
