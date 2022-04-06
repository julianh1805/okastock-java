package com.julianhusson.okastock.email;

import com.julianhusson.okastock.exception.MailSenderException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.properties.mail.sender}")
    private String from;

    @Value("${spring.mail.properties.mail.url}")
    private String url;

    @Async
    public void send(String to, String name, String token){
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(generateEmail(name, token), true);
            helper.setTo(to);
            helper.setSubject("Okastock - Véfifiez votre compte");
            helper.setFrom(from);
            mailSender.send(mimeMessage);
        } catch (MessagingException e){
            throw new MailSenderException("Une erreur s'est produit pendant l'envoie du message.");
        }
    }

    private String generateEmail(String name, String token) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", name);
        variables.put("link", url + "/api/v1/auth/confirm?token=" + token);
        return this.templateEngine.process("mail", new Context(Locale.getDefault(), variables));
    }
}
