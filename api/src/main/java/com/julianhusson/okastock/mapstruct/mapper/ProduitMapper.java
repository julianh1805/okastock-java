package com.julianhusson.okastock.mapstruct.mapper;

import com.julianhusson.okastock.mapstruct.dto.ProduitDTO;
import com.julianhusson.okastock.mapstruct.dto.ProduitPostDTO;
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
