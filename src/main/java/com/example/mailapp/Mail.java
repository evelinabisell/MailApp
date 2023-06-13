package com.example.mailapp;

import java.io.File;
import java.util.ArrayList;

/**
 * Class for e-mails that hold header info (ID, from, to, subject), content and a list of attachments.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class Mail {
    private String id;
    private String from;
    private String subject;
    private String cc;
    private String content;
    private ArrayList<File> attachments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<File> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<File> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "From: " + from + "\nSubject: " + subject + cc;
    }
}
