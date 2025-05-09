package com.example.chatbotfordocumentation.ui.chat;

public class ChatMessage {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;

    private String message;
    private int type; // 1: sent, 2: received

    public ChatMessage(String message, int type){
        this.message = message;
        this.type = type;
    }

    public String getMessage(){
        return message;
    }

    public int getType(){
        return type;
    }
}
