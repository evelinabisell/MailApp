package com.example.mailapp;

import com.sun.mail.imap.IMAPFolder;
import javafx.scene.control.Alert;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Class for receiving e-mails through Googles mail servers.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class MailReceiver {
    private final String host = "imap.gmail.com";
    private final String port = "993";

    private final String username;
    private final String password;
    private final int numMessages;

    private final Session session;

    /**
     * Constructor that sets username, password and number of e-mails to receive, then creates a
     * session that's used for fetching them.
     *
     * @param username the gmail-address to receive e-mails from
     * @param password the app password to the gmail account to receive e-mails from
     * @param numMessages the number of e-mails to receive
     */
    public MailReceiver(String username, String password, int numMessages) {
        this.username = username;
        this.password = password;
        this.numMessages = numMessages;

        this.session = getImapSession();
    }

    /**
     * Receive a number of e-mails from a gmail inbox and return them as Mail objects in a list.
     *
     * @return ArrayList with Mail objects
     */
    public ArrayList<Mail> receiveMails() {
        ArrayList<Mail> mails = new ArrayList<>();

        try {
            Store store = session.getStore("imap");
            store.connect(host, Integer.parseInt(port), username, password);

            IMAPFolder inbox = (IMAPFolder)store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Receive the last number of messages and put them with the most recent one first
            Message[] messageArray = inbox.getMessages(inbox.getMessageCount()-(numMessages-1), inbox.getMessageCount());
            ArrayList<Message> messages = new ArrayList<>(Arrays.asList(messageArray));
            Collections.reverse(messages);
            mails = constructMails(messages);

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Can not connect to Gmail. Check username and password").show();
        }
        return mails;
    }

    // Take a list of messages, save their info and content to mail objects and return them in a list
    private ArrayList<Mail> constructMails(ArrayList<Message> messages) {
        ArrayList<Mail> mails = new ArrayList<>();

        for (Message message : messages) {
            // Typecast to MimeMessage to be able to use .getMessageID()
            MimeMessage mimeMessage = (MimeMessage) message;

            // Create a new mail object and set its attributes
            Mail mail = new Mail();
            try {
                // Set mail ID, who it's from, the subject and the CC list
                String id = mimeMessage.getMessageID();
                mail.setId(id);
                Address[] fromAddress = mimeMessage.getFrom();
                String from = fromAddress[0].toString();
                mail.setFrom(from);
                String subject = mimeMessage.getSubject();
                mail.setSubject(subject);
                mail.setCc(makeCcList(mimeMessage));
                // Receive mail content
                receiveMailContent(mimeMessage, mail);

            } catch (Exception e) {
                System.err.println("Problem displaying e-mail ");
                e.printStackTrace();
            }
            mails.add(mail);
        }
        return mails;
    }

    // Take a message and a mail object, then add content and attachments from the message to the mail object
    private void receiveMailContent(Message message, Mail mail) {
        ArrayList<File> attachments = new ArrayList<>();
        StringBuilder mailContent = new StringBuilder();

        try {
            // If the original message only contained plain text or HTML, add the content to stringbuilder
            if (message.isMimeType("text/*")) {
                mailContent.append(message.getContent());
            }
            // If there's both HTML and plain text parts, add content of only HTML parts to stringbuilder
            if (message.isMimeType("multipart/alternative")) {
                ArrayList<BodyPart> mailParts = new ArrayList<>();
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(i);
                    mailParts.add(bodyPart);
                }
                mailContent.append(onlyKeepHTML(mailParts));
            }
            // If it's a message with attachments
            else if (message.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) message.getContent();

                // Get the text content that's in the first body part and append to the stringbuilder
                MimeBodyPart textBodyPart = (MimeBodyPart) multipart.getBodyPart(0);
                mailContent.append(getText(textBodyPart));

                // iterate over the remaining body parts and save the files and add them to a list of attachments
                // @TODO Don't download all attached files on receive (unsafe), let user choose which files to download
                for (int i = 1; i < multipart.getCount(); i++) {
                    MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(i);
                    String fileName = bodyPart.getFileName();
                    File file = new File(fileName);
                    bodyPart.saveFile(file);
                    attachments.add(file);
                }
            }
        } catch (Exception e) {
            System.err.println("Problem displaying e-mail content ");
            e.printStackTrace();
        }
        // Add the list of attachments and the content to the mail object and return it
        mail.setAttachments(attachments);
        mail.setContent(mailContent.toString());
    }

    // Take a list of mail parts and, if it contains HTML, get rid of plain text and return the HTML text as a string
    private String onlyKeepHTML(List<BodyPart> parts) throws MessagingException, IOException {
        StringBuilder text = new StringBuilder();
        // If the list contains HTML, remove text parts
        if (containsHTML(parts)) {
            parts.removeIf(o -> {
                try {
                    return o.isMimeType("text/plain");
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return false;
            });
        }
        // Iterate over the remaining body parts and add them to a string builder
        for (BodyPart part : parts) {
            text.append(part.getContent().toString());
        }
        return text.toString();
    }

    // Check if a list of mail body parts contains any HTML parts
    private boolean containsHTML(List<BodyPart> parts) {
        return parts.stream().anyMatch(part -> {
            try {
                return part.isMimeType("text/html");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    // Make a nice formatted string of the messages CC list (if there is any) and return it
    private String makeCcList(Message message) throws MessagingException {
        Address[] ccList = message.getRecipients(MimeMessage.RecipientType.CC);
        StringBuilder cc = new StringBuilder();
        if (ccList != null ) {
            cc.append("Cc: ");
            for (Address ccAddress : ccList) {
                cc.append(ccAddress.toString()).append(", ");
            }
            cc.setLength(cc.length() - 2);
        }
        return cc.toString();
    }

    // Set up properties, create a session with them and return the session
    private Session getImapSession(){
        Properties properties = new Properties();
        properties.setProperty("mail.store.protocol", "imap");
        properties.setProperty("mail.imap.host", host);
        properties.setProperty("mail.imap.port", port);
        properties.setProperty("mail.imap.ssl.enable", "true");

        Session session = Session.getDefaultInstance(properties, null);
        session.setDebug(true);
        return session;
    }

    // Return text contents (HTML over plain) of an email part through recursion
    // Needed this to be able to get the text content from a message with attachments, but not sure exactly how it works
    // Code taken from: https://javaee.github.io/javamail/FAQ#hasattach
    private String getText(Part part) throws MessagingException, IOException {
        if (part.isMimeType("text/*")) {
            return part.getContent().toString();
        }
        if (part.isMimeType("multipart/alternative")) {
            Multipart multipart = (Multipart) part.getContent();
            String text = null;
            for (int i = 0; i < multipart.getCount(); i++) {
                Part bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bodyPart);
                    continue;
                } else if (bodyPart.isMimeType("text/html")) {
                    String string = getText(bodyPart);
                    if (string != null)
                        return string;
                } else {
                    return getText(bodyPart);
                }
            }
            return text;
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                String string = getText(multipart.getBodyPart(i));
                if (string != null)
                    return string;
            }
        }
        return null;
    }
}