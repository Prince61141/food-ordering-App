package com.example.foodorderingapp.Helper;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class GMailSender {

    private final String senderEmail;
    private final String senderPassword;

    public GMailSender(String email, String password) {
        this.senderEmail = email;
        this.senderPassword = password;
    }

    public void sendMail(String recipientEmail, String subject, String htmlContent) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "Food Delivery App"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            // Set content type to text/html for HTML email
            message.setContent(htmlContent, "text/html");
            Transport.send(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
