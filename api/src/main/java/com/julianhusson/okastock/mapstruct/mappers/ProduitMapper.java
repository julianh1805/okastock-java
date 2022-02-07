package com.julianhusson.okastock.mapstruct.mappers;

import com.julianhusson.okastock.mapstruct.dtos.ProduitDTO;
import com.julianhusson.okastock.mapstruct.dtos.ProduitPostDTO;
import com.julianhusson.okastock.produit.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProduitMapper{
    @Mapping(target = "categorie", source = "categorie.nom")
    ProduitDTO produitToProduitDTO(Produit produit);

    @Mapping(target = "categorie", expression = "java(null)")
    Produit produitPostDTOToProduit(ProduitPostDTO produitPostDTO);
}
