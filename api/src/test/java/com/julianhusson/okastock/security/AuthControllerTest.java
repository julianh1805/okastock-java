package com.julianhusson.okastock.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper mapper;
    private final String URI = "/api/v1/auth";

    @Test
    void itShouldRegister() throws Exception {
        UtilisateurPostDTO utilisateurPostDTO =
                new UtilisateurPostDTO("Test", 11110987654321L, 44000, 685487966L, "http://www.testf.com", "-", true, "testf@test.com", "1234AZER");
        String json = mapper.writeValueAsString(utilisateurPostDTO);
        mockMvc.perform(post(URI + "/register").content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nom").value("Test"))
                .andExpect(jsonPath("$.siret").value(11110987654321L))
                .andExpect(jsonPath("$.codePostal").value(44000))
                .andExpect(jsonPath("$.telephone").value(685487966L))
                .andExpect(jsonPath("$.site").value("http://www.testf.com"))
                .andExpect(jsonPath("$.logo").value("-"))
                .andExpect(jsonPath("$.rgpd").isBoolean())
                .andExpect(jsonPath("$.email").value("testf@test.com"));
    }
}