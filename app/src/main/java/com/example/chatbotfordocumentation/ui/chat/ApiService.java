package com.example.chatbotfordocumentation.ui.chat;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface  ApiService {

    @Headers("Content-Type: application/json")
    @GET("/ask") // путь к твоему endpoint на сервере
    Call<ApiResponse> sendMessage(@Body ApiRequest request);
}
