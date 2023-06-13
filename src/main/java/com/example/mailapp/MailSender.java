package com.example.mailapp;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Class with static methods for sending e-mails through Googles mail servers with SSL or TSL encryption.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class MailSender {
    private static final String host = "smtp.gmail.com";
    private static final String TLSPort = "587";
    private static final String SSLPort = "465";

    /**
     * Send an e-mail with SSL or TSL encryption and return true if it succeeded.
     *
     * @param encryption a String saying which encryption type to use "SSL" or "TLS"
     * @param to the e-mail address to send the e-mail to
     * @param subject the subject of the e-mail
     * @param text the text content of the e-mail
     * @param username the gmail address of the user sending the e-mail
     * @param password the gmail app password of the user sending the e-mail
     * @param files the e-mail attachments
     * @return true if sending the e-mail seems to have succeeded
     */
    public static boolean sendMail(String encryption, String to, String subject, String text, String username,
                                   String password, ArrayList<File> files) {
        Properties properties = new Properties();
        if (encryption.equals("SSL")) {
            properties = setPropsSSL();
        } else if (encryption.equals("TLS")) {
            properties = setPropsTLS();
        } else {
            System.err.println("Something went wrong, encryption type not specified");
        }

        // Create session and authenticate with username and password specified
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Try to create and send a message with info from whom, to whom, its subject and text
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, "UTF-8");
            message.setText(text, "UTF-8");
            message.setContent(text, "text/plain; charset=UTF-8");

            // If there are attachments, make message content a multipart and add text and attached files to it
            if (!files.isEmpty()) {
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setHeader("Content-Type", "text/plain; charset=UTF-8");
                message.setText(text, "UTF-8");
                mimeBodyPart.setContent(text, "text/plain; charset=UTF-8");
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                for (File file : files) {
                    mimeBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file);
                    mimeBodyPart.setDataHandler(new DataHandler(source));
                    mimeBodyPart.setFileName(file.getName());
                    multipart.addBodyPart(mimeBodyPart);
                }
                message.setContent(multipart);
            }

            Transport.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Return properties needed for creating session to Googles mail servers with SSL encryption
    private static Properties setPropsSSL() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", SSLPort);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.port", SSLPort);
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.timeout", "5000");
        properties.setProperty("mail.smtp.connectiontimeout", "5000");

        return properties;
    }

    // Return properties needed for creating session to Googles mail servers with TLS encryption
    private static Properties setPropsTLS() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", TLSPort);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.timeout", "5000");
        properties.setProperty("mail.smtp.connectiontimeout", "5000");

        return properties;
    }
}
