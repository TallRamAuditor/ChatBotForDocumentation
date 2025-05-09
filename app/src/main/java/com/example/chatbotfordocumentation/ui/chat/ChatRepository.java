package com.example.chatbotfordocumentation.ui.chat;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatRepository {
    private static ChatRepository instance;
    private List<Chat> chats = new ArrayList<>();
    private SharedPreferences prefs;

    private ChatRepository(Context context) {
        prefs = context.getSharedPreferences("chat_prefs", Context.MODE_PRIVATE);
        loadChats();
    }
    public Optional<Chat> getChatById(String chatId) {
        return chats.stream()
                .filter(chat -> chat.getId().equals(chatId))
                .findFirst();
    }

    public static synchronized ChatRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ChatRepository(context);
        }
        return instance;
    }

    public List<Chat> getChats() {
        return new ArrayList<>(chats);
    }

    public Chat createNewChat() {
        Chat chat = new Chat("Чат " + (chats.size() + 1));
        chats.add(chat);
        saveChats();
        return chat;
    }

    private void saveChats() {
        // Реализация сохранения в SharedPreferences
    }

    private void loadChats() {
        // Реализация загрузки из SharedPreferences
    }
}
