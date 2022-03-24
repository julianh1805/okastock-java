package com.julianhusson.okastock.produit;

import com.julianhusson.okastock.mapstruct.dto.ProduitPostDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql({"/categorie-data.sql", "/utilisateur-data.sql", "/produit-data.sql", "/role-data.sql", "/utilisateur-roles-data.sql"})
@Transactional
class ProduitControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    private final String URI = "/api/v1/products";

    @Test
    void itShouldGetProducts() throws Exception {
        mockMvc.perform(get(URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void itShouldGetProduct() throws Exception {
        String produitId = "e59ed17d-db7d-4d24-af6c-5154b3f72df0";
        mockMvc.perform(get(URI + "/" + produitId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produitId))
                .andExpect(jsonPath("$.titre").value("Titre"))
                .andExpect(jsonPath("$.description").value("Petite description"))
                .andExpect(jsonPath("$.prix").value(10.27))
                .andExpect(jsonPath("$.quantite").value(8))
                .andExpect(jsonPath("$.categorie").value("meubles"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.utilisateur").exists());
    }

    @Test
    @WithMockUser(value = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe")
    void itShouldAddProduct() throws Exception {
        ProduitPostDTO produitPostDTO = new ProduitPostDTO("Titre", "Description", 0.1, 19, "meubles");
        String json = mapper.writeValueAsString(produitPostDTO);
        mockMvc.perform(post(URI).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void itShouldThrowUnauthorizedExceptionWhenAddProduct() throws Exception {
        ProduitPostDTO produitPostDTO = new ProduitPostDTO("Titre", "Description", 0.1, 19, "meubles");
        String json = mapper.writeValueAsString(produitPostDTO);
        mockMvc.perform(post(URI).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe")
    void itShouldUpdateProduct() throws Exception {
        String produitId = "e59ed17d-db7d-4d24-af6c-5154b3f72df0";
        ProduitPostDTO produitPostDTO = new ProduitPostDTO("Titre update", "Description update", 1, 13, "meubles");
        String json = mapper.writeValueAsString(produitPostDTO);
        mockMvc.perform(put(URI + "/" + produitId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void itShouldThrowUnauthorizedExceptionWhenUpdateProduct() throws Exception {
        String produitId = "e59ed17d-db7d-4d24-af6c-5154b3f72df0";
        ProduitPostDTO produitPostDTO = new ProduitPostDTO("Titre update", "Description update", 1, 13, "meubles");
        String json = mapper.writeValueAsString(produitPostDTO);
        mockMvc.perform(put(URI + "/" + produitId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe")
    void itShouldDeleteProduct() throws Exception {
        String produitId = "e59ed17d-db7d-4d24-af6c-5154b3f72df0";
        mockMvc.perform(delete(URI + "/" + produitId))
                .andExpect(status().isNoContent());
    }

    @Test
    void itShouldThrowUnauthorizedExceptionWhenDeleteProduct() throws Exception {
        String produitId = "e59ed17d-db7d-4d24-af6c-5154b3f72df0";
        mockMvc.perform(delete(URI + "/" + produitId))
                .andExpect(status().isUnauthorized());
    }
}