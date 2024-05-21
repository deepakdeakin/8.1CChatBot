package com.jk.apps.chatbot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jk.apps.chatbot.adapter.ChatAdapter;
import com.jk.apps.chatbot.databinding.ActivityMainBinding;
import com.jk.apps.chatbot.model.ChatModel;
import com.jk.apps.chatbot.network.RetroClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ChatAdapter adapter;
    String username = "Username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent() != null & getIntent().hasExtra("username")) {
            username = getIntent().getStringExtra("username");
        }
        setAdapter();
        onClick();
    }

    public boolean checkInternet(Context activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    public void onClick() {
        binding.btnSend.setOnClickListener(v -> {
            if (binding.edText.getText().toString().equalsIgnoreCase("")) {
                binding.edText.setError("Please Enter Something");
            } else if (!checkInternet(this)) {
                Toast.makeText(MainActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
            } else {
                String data = binding.edText.getText().toString();
                adapter.addItem(new ChatModel(binding.edText.getText().toString(), "", true));
                disableButtons();
                binding.rvChat.scrollToPosition(0);
                sendMessage(data);
            }
        });
    }

    public void disableButtons() {
        binding.edText.setText("");
        binding.btnSend.setEnabled(false);
        binding.edText.setEnabled(false);
    }

    public void enableButtons() {
        binding.btnSend.setEnabled(true);
        binding.edText.setEnabled(true);
    }

    public void setAdapter() {
        adapter = new ChatAdapter(this, username, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        binding.rvChat.setLayoutManager(layoutManager);
        binding.rvChat.setAdapter(adapter);
        adapter.addItem(new ChatModel("", "Welcome "+ username, false));
    }

    public void sendMessage(String data) {

        try {
            JSONObject object = new JSONObject();
            object.put("userMessage", data);
            object.put("chatHistory", adapter.getJsonArray());
            RetroClient.getInstance().getApi().sendMessage(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString())).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject object1 = new JSONObject(response.body().string());
                        adapter.addAnswer(object1.getString("message"));
                        enableButtons();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        enableButtons();
                        adapter.removeAnswer();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    enableButtons();
                    adapter.removeAnswer();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            enableButtons();
            adapter.removeAnswer();
        }
    }
}