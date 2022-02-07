package com.julianhusson.okastock.mapstruct.mappers;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.mapstruct.dtos.*;
import com.julianhusson.okastock.produit.Produit;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface CategorieMapper {
    CategorieDTO categorieToCategorieDTO(Categorie categorie);
    Set<CategorieProduitDTO> listProduitsToListCategorieProduitsDTO(Set<Produit> produit);
}
