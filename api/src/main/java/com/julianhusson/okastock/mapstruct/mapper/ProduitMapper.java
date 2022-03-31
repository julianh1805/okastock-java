package com.julianhusson.okastock.mapstruct.mapper;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.mapstruct.dto.*;
import com.julianhusson.okastock.produit.Produit;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface ProduitMapper{
    @Mapping(target = "categorie", source = "categorie.nom")
    ProduitDTO produitToProduitDTO(Produit produit);

    @Mapping(target = "categorie", expression = "java(null)")
    Produit produitPostDTOToProduit(ProduitPostDTO produitPostDTO);

    UtilisateurProduitDTO utilisateurToUtilisateurProduitDTO(Utilisateur utilisateur);

}
