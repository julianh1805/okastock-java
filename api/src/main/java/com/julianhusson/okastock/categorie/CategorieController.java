package com.julianhusson.okastock.categorie;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public record CategorieController(CategorieService categorieService) {

    @Operation(summary = "Pour récupérer un object Catégorie grâce son nom")
    @GetMapping("{nom}")
    public ResponseEntity<Categorie> getCategoryByNom(@PathVariable String nom){
        return new ResponseEntity<>(categorieService.findByNom(nom), HttpStatus.OK);
    }
}
