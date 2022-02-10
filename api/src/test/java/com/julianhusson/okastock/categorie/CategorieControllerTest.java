package com.julianhusson.okastock.categorie;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql("/categorie-data.sql")
class CategorieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldGetCategoryByNom() throws Exception {
        String URI = "/api/v1/categories";
        String categorie = "meubles";
        mockMvc.perform(get(URI + "/" + categorie))
                .andExpect(jsonPath("$.nom").value(categorie))
                .andExpect(jsonPath("$.produits").isArray());
    }
}