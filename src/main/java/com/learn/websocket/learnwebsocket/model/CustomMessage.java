package com.learn.websocket.learnwebsocket.model;

public class CustomMessage {
    private String sender;
    private String content;

    private String sendDate;

    private String reciever;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public CustomMessage() {
    }

    public CustomMessage(String sender, String content, String sendDate, String reciever) {
        this.sender = sender;
        this.content = content;
        this.sendDate = sendDate;
        this.reciever = reciever;
    }

    @Override
    public String toString() {
        return "CustomMessage [sender=" + sender + ", content=" + content + ", sendDate=" + sendDate + ", reciever="
                + reciever + "]";
    }

    
}
