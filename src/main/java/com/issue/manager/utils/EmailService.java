package com.issue.manager.utils;

import com.issue.manager.models.constants.MessageConstants;
import com.issue.manager.models.project.EmailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailService {

    @Value("${email.username}")
    private String emailUsername;

    @Value("${email.password}")
    private String emailPassword;

    public void sendEmail(String email, EmailType type, Map<String, Object> properties) throws Exception {
        Properties props = setProperties();

        Session session = Session.getDefaultInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                emailUsername,
                                emailPassword);
                    }
                });

        MimeMessage message = composeMessage(session, email, type, properties);

        Transport transport = session.getTransport();
        transport.connect();
        Transport.send(message);
        transport.close();
    }

    private static Properties setProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.starttls.required","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.ssl.trust","smtp.gmail.com");
        props.put("mail.smtp.port","465");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.debug","true");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return props;
    }

    private MimeMessage composeMessage(Session session, String email, EmailType type, Map<String, Object> properties) throws MessagingException {
        MimeMessage message = new MimeMessage(session);

        InternetAddress addressFrom = new InternetAddress(System.getenv("EMAIL_ADDRESS"));
        message.setSender(addressFrom);

        switch (type) {
            case NEW_USER_INVITED_TO_PROJECT -> {
                message.setSubject(MessageConstants.INVITED_TO_PROJECT_SUBJECT);
                message.setContent(String.format(MessageConstants.INVITED_TO_PROJECT_EMAIL_MESSAGE, properties.get("projectName"),
                        properties.get("link")), "text/html");
            }
            default -> {
                log.debug("Email type not recognized: " + type);
            }
        }

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

        return message;
    }
}