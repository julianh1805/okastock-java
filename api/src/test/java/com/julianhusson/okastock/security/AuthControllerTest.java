package com.julianhusson.okastock.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.julianhusson.okastock.configuration.SmtpServerRule;
import com.julianhusson.okastock.configuration.WithMockCustomUser;
import com.julianhusson.okastock.email.EmailService;
import com.julianhusson.okastock.mapstruct.dto.UtilisateurPostDTO;
import com.julianhusson.okastock.storage.StorageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @MockBean private StorageService storageService;
    private final String URI = "/api/v1/auth";

    @BeforeAll
    public static void setUp() {
        GreenMail smtpServer;
        smtpServer = new GreenMail(new ServerSetup(1025, null, "smtp"));
        smtpServer.setUser("username", "password");
        smtpServer.start();
    }

    @Test
    void itShouldRegister() throws Exception {
        MockMultipartFile logo = new MockMultipartFile("logo", "logo.txt", "text/plain", "some xml".getBytes());
        UtilisateurPostDTO utilisateurPostDTO =
                new UtilisateurPostDTO("Test", 11110987654321L, 44000, 685487966L, "http://www.testf.com", true, "testf@test.com", "1234AZER");
        when(storageService.postLogo(logo)).thenReturn("e59ed17d-db7c-4d24-af6c-5154b3f72def");
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(URI + "/register").file(logo).contentType(MediaType.MULTIPART_FORM_DATA)
                .flashAttr("utilisateurPostDTO", utilisateurPostDTO);
        mockMvc.perform(builder)
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