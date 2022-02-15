package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.mapstruct.dtos.ProduitDTO;
import com.julianhusson.okastock.mapstruct.dtos.ProduitPostDTO;
import com.julianhusson.okastock.mapstruct.mappers.ProduitMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
public record ProduitController(ProduitService produitService, ProduitMapper produitMapper) {

    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getProducts(){
        List<Produit> produits = this.produitService.getAll();
        return new ResponseEntity<>(
                produits.stream().map(produitMapper::produitToProduitDTO).toList(),
                HttpStatus.OK);
    }

    @GetMapping("{produitId}")
    public ResponseEntity<ProduitDTO> getProduct(@PathVariable UUID produitId){
        return new ResponseEntity<>(
                produitMapper.produitToProduitDTO(this.produitService.getById(produitId)),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addProduct(@RequestBody ProduitPostDTO produitPostDTO){
        this.produitService.upSert(produitMapper.produitPostDTOToProduit(produitPostDTO), produitPostDTO.getCategorie());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{produitId}")
    public ResponseEntity<Produit> updateProduct(@PathVariable UUID produitId, @RequestBody ProduitPostDTO produitPostDTO){
        Produit produit = produitMapper.produitPostDTOToProduit(produitPostDTO);
        produit.setId(produitId);
        this.produitService.upSert(produit, produitPostDTO.getCategorie());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{produitId}")
    public ResponseEntity updateProduct(@PathVariable UUID produitId){
        this.produitService.delete(produitId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
