package com.julianhusson.okastock.utilisateur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julianhusson.okastock.configuration.WithMockCustomUser;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.storage.StorageService;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@EnableJpaAuditing
@Sql( {"/utilisateur-data.sql", "/categorie-data.sql", "/produit-data.sql"})
@Transactional
class UtilisateurControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;

    @MockBean private StorageService storageService;
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
                .andExpect(jsonPath("$.logo").value("e59ed17d-db7c-4d24-af6c-5154b3f72def"))
                .andExpect(jsonPath("$.rgpd").isBoolean())
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.produits").exists())
                .andExpect(jsonPath("$.produits[0]").exists())
                .andExpect(jsonPath("$.produits[0].utilisateur").doesNotExist())
                .andExpect(jsonPath("$.motDePasse").doesNotExist());
    }

    @Test
    @WithMockCustomUser
    void itShouldUpdateUser() throws Exception {
        String utilisateurId = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
        String logoId = "e59ed17d-db7c-4d24-af6c-5154b3f72def";
        UtilisateurPostDTO utilisateurPostDTO =
                new UtilisateurPostDTO("Test 2", 22345678910111L, 44200, 666666662L, "http://www.test2.com", true, "test2@test.com", null);
        String json = mapper.writeValueAsString(utilisateurPostDTO);
        MockMultipartFile logo = new MockMultipartFile("logo", "logo.txt", "text/plain", "some xml".getBytes());
        when(storageService.upsertLogo(logo, Optional.ofNullable(logoId))).thenReturn(logoId);
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(URI + "/" + utilisateurId).file(logo).contentType(MediaType.MULTIPART_FORM_DATA).with(new RequestPostProcessor() {
                    @Override
                    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                        request.setMethod("PUT");
                        return request;
                    }
                })
                .flashAttr("utilisateurPostDTO", utilisateurPostDTO);
        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(utilisateurId))
                .andExpect(jsonPath("$.nom").value("Test 2"))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.siret").value(22345678910111L))
                .andExpect(jsonPath("$.codePostal").value(44200))
                .andExpect(jsonPath("$.telephone").value(666666662))
                .andExpect(jsonPath("$.site").value("http://www.test2.com"))
                .andExpect(jsonPath("$.logo").value(logoId))
                .andExpect(jsonPath("$.rgpd").isBoolean())
                .andExpect(jsonPath("$.email").value("test2@test.com"))
                .andExpect(jsonPath("$.motDePasse").doesNotExist());
    }

    @Test
    void itShouldThrowForbiddenExceptionWhenUpdateUser() throws Exception {
        String utilisateurId = "e59ed17d-db7c-4d24-af6c-5154b3f72dfe";
        UtilisateurPostDTO utilisateurPostDTO =
                new UtilisateurPostDTO("Test 2", 22345678910111L, 44200, 666666662L, "http://www.test2.com", true, "test2@test.com", null);
        String json = mapper.writeValueAsString(utilisateurPostDTO);
        mockMvc.perform(put(URI + "/" + utilisateurId).content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockCustomUser
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