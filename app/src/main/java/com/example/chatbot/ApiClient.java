package com.example.chatbot;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;
import retrofit2.Call;

public class ApiClient {
    private static final String BASE_URL = "http://<YOUR_BACKEND_URL>/";
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
        @POST("chat")
        @FormUrlEncoded
        Call<String> sendMessage(@Field("message") String message);
    }
}
