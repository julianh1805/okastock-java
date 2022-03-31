package com.julianhusson.okastock.mapstruct.mapper;

import com.julianhusson.okastock.categorie.Categorie;
import com.julianhusson.okastock.mapstruct.dto.ProduitUtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurDTO;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.produit.Produit;
import com.julianhusson.okastock.utilisateur.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface UtilisateurMapper {
    Utilisateur utilisateurPostDTOToUtilisateur(UtilisateurPostDTO utilisateurPostDTO);

    UtilisateurDTO utilisateurToUtilisateurDTO(Utilisateur utilisateur);

    List<ProduitUtilisateurDTO> produitsToProduitsUtilisateurDTO(List<Produit> produits);

    @Mapping(target = "categorie", source = "categorie.nom")
    ProduitUtilisateurDTO produitToProduitUtilisateurDTO(Produit produit);

    String map(Categorie categorie);
}
