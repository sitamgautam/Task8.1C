package com.example.chatbot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChatActivity extends AppCompatActivity {

    EditText userInput;
    Button sendButton;
    TextView chatDisplay;

    // Increase timeout to avoid bot timeout errors
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    private static final String BASE_URL = "http://10.0.2.2:5000/chat";
    private static final String TYPING_PLACEHOLDER = "Bot is typing...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);
        chatDisplay = findViewById(R.id.chatDisplay);

        sendButton.setOnClickListener(v -> {
            String message = userInput.getText().toString().trim();
            if (!message.isEmpty()) {
                chatDisplay.append("You: " + message + "\n");
                chatDisplay.append("Bot: " + TYPING_PLACEHOLDER + "\n");
                sendMessageToBackend(message);
                userInput.setText("");
            }
        });
    }

    private void sendMessageToBackend(String message) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{\"message\":\"" + message + "\"}";
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    replaceLastBotMessage("Bot: ERROR - " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String botReply = response.body() != null ? response.body().string() : "No response";

                runOnUiThread(() -> {
                    replaceLastBotMessage("Bot: " + botReply);
                });
            }
        });
    }

    // Replace the last "Bot is typing..." with the real message or error
    private void replaceLastBotMessage(String newBotMessage) {
        String currentText = chatDisplay.getText().toString();
        int lastIndex = currentText.lastIndexOf("Bot: " + TYPING_PLACEHOLDER);
        if (lastIndex != -1) {
            String updatedText = currentText.substring(0, lastIndex) + newBotMessage + "\n";
            chatDisplay.setText(updatedText);
        } else {
            chatDisplay.append(newBotMessage + "\n");
        }
    }
}
