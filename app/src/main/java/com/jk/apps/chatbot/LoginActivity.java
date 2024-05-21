package com.jk.apps.chatbot;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jk.apps.chatbot.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        onClick();
    }

    public void onClick() {
        binding.btnGo.setOnClickListener(v -> {
            if (binding.edUserName.getText().toString().equalsIgnoreCase("")) {
                binding.edUserName.setError("Please Enter Username");
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("username",binding.edUserName.getText().toString());
                startActivity(intent);
            }
        });
    }
}