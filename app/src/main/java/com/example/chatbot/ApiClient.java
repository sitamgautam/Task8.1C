package com.example.chatbot;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.Call;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:5000/";
    private static Retrofit retrofit;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    public interface ApiService {
        @Headers("Content-Type: application/json")
        @POST("chat")
        Call<String> sendMessage(@Body Message message);
    }
}

