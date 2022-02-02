package com.julianhusson.okastock.categorie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;
    private final String URI = "/api/v1/categories";

    @Test
    @Sql("/categorie-data.sql")
    void getCategoryByNom() throws Exception {
        String categorie = "meubles";
        mockMvc.perform(get(URI + "/" + categorie))
                .andExpect(jsonPath("$.nom", is(categorie)));
    }
}