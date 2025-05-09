package com.example.chatbotfordocumentation.ui.chat;

public class ApiRequest {
    private String question;

    public ApiRequest(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }
}
