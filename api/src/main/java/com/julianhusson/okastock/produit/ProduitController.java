package com.julianhusson.okastock.produit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/produits")
public record ProduitController(ProduitService produitService) {

    @GetMapping
    public ResponseEntity<List<Produit>> getProducts(){
        return new ResponseEntity<>(this.produitService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{produitId}")
    public ResponseEntity<Produit> getProduct(@PathVariable UUID produitId){
        return new ResponseEntity<>(this.produitService.getById(produitId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addProduct(@RequestBody Produit produit){
        this.produitService.add(produit);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{produitId}")
    public ResponseEntity<Produit> updateProduct(@PathVariable UUID produitId, @RequestBody Produit produit){
        this.produitService.update(produit);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{produitId}")
    public ResponseEntity updateProduct(@PathVariable UUID produitId){
        this.produitService.delete(produitId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
