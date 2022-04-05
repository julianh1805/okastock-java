package com.julianhusson.okastock.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.julianhusson.okastock.configuration.WithMockCustomUser;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql({"/role-data.sql", "/utilisateur-data.sql", "/validation-token-data.sql"})
@Transactional
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
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void itShouldLogin() throws Exception {
        this.mockMvc
                .perform(post(URI + "/login").header(HttpHeaders.AUTHORIZATION,
                        "Basic " + Base64Utils.encodeToString("test@test.com:1234AZER".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @WithMockCustomUser
    void itShouldConfirm() throws Exception {
        this.mockMvc
                .perform(get(URI + "/confirm?token=00b02bb5-0424-4251-8a23-d1030cb52754"))
                .andExpect(status().isNoContent());
    }
}