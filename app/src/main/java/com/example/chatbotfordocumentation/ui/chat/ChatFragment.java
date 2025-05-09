package com.example.chatbotfordocumentation.ui.chat;
import com.example.chatbotfordocumentation.ui.chat.ApiService;
import com.example.chatbotfordocumentation.ui.chat.ApiRequest;
import com.example.chatbotfordocumentation.ui.chat.ApiResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chatbotfordocumentation.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private static final int PICK_FILE_REQUEST = 1;

    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private ImageButton buttonAddFile;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов
        recyclerViewChat = view.findViewById(R.id.recyclerView_chat);
        editTextMessage = view.findViewById(R.id.editText_message);
        buttonSend = view.findViewById(R.id.button_send);
        buttonAddFile = view.findViewById(R.id.button_add_file);

        // Инициализация списка сообщений и адаптера
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewChat.setAdapter(chatAdapter);

        // Обработка нажатия кнопки отправки
        buttonSend.setOnClickListener(v -> sendMessage());

        // Обработка нажатия кнопки добавления файла
        buttonAddFile.setOnClickListener(v -> openFileChooser());
    }

    // Метод для отправки сообщения
    private void sendMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            // Отображаем отправленное сообщение
            chatMessages.add(new ChatMessage(message, ChatMessage.TYPE_SENT));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
            editTextMessage.setText("");

            // Отправка на сервер
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8000/ask?question=") // замените на свой адрес сервера
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            ApiRequest request = new ApiRequest(message);

            apiService.sendMessage(request).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String botReply = response.body().getResponse();

                        chatMessages.add(new ChatMessage(botReply, ChatMessage.TYPE_RECEIVED));
                        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                        recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                    } else {
                        chatMessages.add(new ChatMessage("Ошибка сервера", ChatMessage.TYPE_RECEIVED));
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    chatMessages.add(new ChatMessage("Ошибка соединения: " + t.getMessage(), ChatMessage.TYPE_RECEIVED));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                }
            });
        } else {
            Toast.makeText(getContext(), "Введите сообщение", Toast.LENGTH_SHORT).show();
        }
    }


    // Метод для открытия выбора файла
    private void openFileChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Можно ограничить типы файлов, например, "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Выберите файл"), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FILE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null){
            Uri fileUri = data.getData();
            String fileName = getFileName(fileUri);
            if(fileName != null){
                // Добавление сообщения с прикрепленным файлом
                chatMessages.add(new ChatMessage("Прикреплен файл: " + fileName, ChatMessage.TYPE_RECEIVED));
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                recyclerViewChat.scrollToPosition(chatMessages.size() - 1);

                // Здесь можно добавить логику загрузки файла или отправки его на сервер
            }
        }
    }

    // Метод для получения имени файла
    private String getFileName(Uri uri){
        String result = null;
        if(uri.getScheme().equals("content")){
            try (android.database.Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)){
                if(cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
