package com.julianhusson.okastock.categorie;

import com.julianhusson.okastock.mapstruct.mappers.CategorieMapper;
import com.julianhusson.okastock.mapstruct.dtos.CategorieDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/categories")
public record CategorieController(CategorieService categorieService, CategorieMapper categorieMapper) {

    @Operation(summary = "Pour récupérer un object catégorie et ses produits grâce son nom")
    @GetMapping("{categorieNom}")
    public ResponseEntity<CategorieDTO> getCategoryByNom(@PathVariable String categorieNom){
        return new ResponseEntity<>(
                categorieMapper.categorieToCategorieDTO(categorieService.findByNom(categorieNom)),
                HttpStatus.OK);
    }
}
