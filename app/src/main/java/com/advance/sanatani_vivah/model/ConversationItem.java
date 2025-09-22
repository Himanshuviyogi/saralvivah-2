package com.advance.sanatani_vivah.model;

public class ConversationItem {
    String id,sender,receiver,content,is_sent_receive,sent_on,photo_url;
    boolean is_checked;

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_sent_receive() {
        return is_sent_receive;
    }

    public void setIs_sent_receive(String is_sent_receive) {
        this.is_sent_receive = is_sent_receive;
    }

    public String getSent_on() {
        return sent_on;
    }

    public void setSent_on(String sent_on) {
        this.sent_on = sent_on;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }
}
