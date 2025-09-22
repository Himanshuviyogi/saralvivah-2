package com.advance.sanatani_vivah.model;

public class Message_item {
    String id,content,read_status,receiver,receiver_delete,sender,sender_delete,sent_on,subject,display_name,image;
    boolean is_check;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isIs_check() {
        return is_check;
    }

    public void setIs_check(boolean is_check) {
        this.is_check = is_check;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver_delete() {
        return receiver_delete;
    }

    public void setReceiver_delete(String receiver_delete) {
        this.receiver_delete = receiver_delete;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender_delete() {
        return sender_delete;
    }

    public void setSender_delete(String sender_delete) {
        this.sender_delete = sender_delete;
    }

    public String getSent_on() {
        return sent_on;
    }

    public void setSent_on(String sent_on) {
        this.sent_on = sent_on;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
