package com.example.chatbotfordocumentation.ui.chat;

import java.util.Date;
import java.util.UUID;

public class Chat {
    private String id;
    private String title;
    private Date createdAt;
    private Date lastMessageTime;

    // Конструктор
    public Chat(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.createdAt = new Date();
        this.lastMessageTime = new Date();
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Date getLastMessageTime() { return lastMessageTime; }
    public void updateLastMessageTime() { this.lastMessageTime = new Date(); }
}
