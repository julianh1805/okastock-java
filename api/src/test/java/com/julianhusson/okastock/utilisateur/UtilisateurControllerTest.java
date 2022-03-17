package com.julianhusson.okastock.utilisateur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@EnableJpaAuditing
@Sql( "/utilisateur-data.sql")
@Transactional
class UtilisateurControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    private final String URI = "/api/v1/users";

    @Test
    void itShouldGetUser() throws Exception {
        String utilisateurId = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
        mockMvc.perform(get(URI + "/" + utilisateurId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(utilisateurId))
                .andExpect(jsonPath("$.nom").value("Test"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.updatedAt").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.siret").value(12345678910111L))
                .andExpect(jsonPath("$.codePostal").value(44300L))
                .andExpect(jsonPath("$.telephone").value(666666666L))
                .andExpect(jsonPath("$.site").value("http://www.test.com"))
                .andExpect(jsonPath("$.logo").value("-"))
                .andExpect(jsonPath("$.rgpd").isBoolean())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.motDePasse").doesNotExist());
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void itShouldUpdateUser() throws Exception {
        String utilisateurId = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
        UtilisateurPostDTO utilisateurPostDTO =
                new UtilisateurPostDTO("Test 2", 22345678910111L, 44200, 666666662L, "http://www.test2.com", "-", true, "test2@test.com", null);
        String json = mapper.writeValueAsString(utilisateurPostDTO);
        mockMvc.perform(put(URI + "/" + utilisateurId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(utilisateurId))
                .andExpect(jsonPath("$.nom").value("Test 2"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.siret").value(22345678910111L))
                .andExpect(jsonPath("$.codePostal").value(44200))
                .andExpect(jsonPath("$.telephone").value(666666662))
                .andExpect(jsonPath("$.site").value("http://www.test2.com"))
                .andExpect(jsonPath("$.logo").value("-"))
                .andExpect(jsonPath("$.rgpd").isBoolean())
                .andExpect(jsonPath("$.email").value("test2@test.com"))
                .andExpect(jsonPath("$.motDePasse").doesNotExist());
    }

    @Test
    void itShouldThrowForbiddenExceptionWhenUpdateUser() throws Exception {
        String utilisateurId = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
        UtilisateurPostDTO utilisateurPostDTO =
                new UtilisateurPostDTO("Test 2", 22345678910111L, 44200, 666666662L, "http://www.test2.com", "-", true, "test2@test.com", null);
        String json = mapper.writeValueAsString(utilisateurPostDTO);
        mockMvc.perform(put(URI + "/" + utilisateurId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "test@test.com")
    void itShouldDeleteUser() throws Exception {
            String utilisateurId = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
            mockMvc.perform(delete(URI + "/" + utilisateurId))
                    .andExpect(status().isNoContent());
    }

    @Test
    void itShouldThrowForbiddenExceptionWhenDeleteUser() throws Exception {
        String utilisateurId = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
        mockMvc.perform(delete(URI + "/" + utilisateurId))
                .andExpect(status().isUnauthorized());
    }
}