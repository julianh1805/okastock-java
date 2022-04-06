package com.julianhusson.okastock.email;

import com.julianhusson.okastock.configuration.SmtpServerRule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
public class EmailServiceTest {

    @Autowired
    private EmailService underTest;

    @Rule
    public SmtpServerRule smtpServerRule = new SmtpServerRule(1025);

    @Value("${spring.mail.properties.mail.sender}")
    private String from;

    @Value("${spring.mail.properties.mail.url}")
    private String url;

    private final String name = "Test";
    private final String token = "test";

    @Test
    public void itShouldSend() throws MessagingException, IOException {
        //Given
        String to = "test@test.com";
        //When
        underTest.send(to, name, token);
        //Then
        MimeMessage[] receivedMessages = smtpServerRule.getMessages();
        assertThat(receivedMessages).hasSize(1);
        MimeMessage current = receivedMessages[0];
        assertThat(Arrays.stream(current.getFrom()).findFirst().get().toString()).isEqualTo(from);
        assertThat(Arrays.stream(current.getRecipients(Message.RecipientType.TO)).findFirst().get().toString()).isEqualTo(to);
        assertThat(current.getSubject()).isEqualTo("Okastock - Véfifiez votre compte");
        getTemplate(current.getContent().toString());
    }

    private void getTemplate(String content) {
        Document doc = Jsoup.parse(content);
        assertThat(doc.select("span#name").first().text()).isEqualTo(name);
        assertThat(doc.select("a#link").first().attr("href")).isEqualTo(url + "/api/v1/auth/confirm?token=" + token);
    }

}