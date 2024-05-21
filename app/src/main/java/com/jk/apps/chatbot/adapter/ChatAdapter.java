package com.jk.apps.chatbot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jk.apps.chatbot.databinding.ViewChatBinding;
import com.jk.apps.chatbot.model.ChatModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    Context context;
    List<ChatModel> models = new ArrayList<>();
    String username;

    public ChatAdapter(Context context, String username, List<ChatModel> models) {
        this.context = context;
        this.models = models;
        this.username = username;
    }

    public void addItem(ChatModel model) {
        models.add(0, model);
        notifyItemInserted(0);
    }

    public JSONArray getJsonArray() {
        try {
            JSONArray array = new JSONArray();
            for (int j = 0; j < models.size(); j++) {
                if (!models.get(j).getTxtBot().equalsIgnoreCase("") && !models.get(j).getTxtUser().equalsIgnoreCase("")) {
                    JSONObject object = new JSONObject();
                    object.put("User", models.get(j).getTxtUser());
                    object.put("Llama", models.get(j).getTxtBot());
                    array.put(object);
                }
                if (array.length() >= 3) {
                    break;
                }
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    public void addAnswer(String answer) {
        models.get(0).setLoading(false);
        models.get(0).setTxtBot(answer);
        notifyItemChanged(0);
    }

    public void removeAnswer() {
        models.get(0).setTxtBot("");
        models.get(0).setLoading(false);
        notifyItemChanged(0);
    }


    public class ChatHolder extends RecyclerView.ViewHolder {
        ViewChatBinding binding;

        public ChatHolder(@NonNull ViewChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(ViewChatBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        ChatModel model = models.get(position);
        holder.binding.llUser.setVisibility(model.getTxtUser().equalsIgnoreCase("") ? View.GONE : View.VISIBLE);
        if (model.isLoading()) {
            holder.binding.llBot.setVisibility(View.VISIBLE);
            holder.binding.spinKit.setVisibility(View.VISIBLE);
            holder.binding.txtBot.setText("");
        } else if (model.getTxtBot().equalsIgnoreCase("")) {
            holder.binding.spinKit.setVisibility(View.GONE);
            holder.binding.llBot.setVisibility(View.GONE);
        } else {
            holder.binding.llBot.setVisibility(View.VISIBLE);
            holder.binding.spinKit.setVisibility(View.GONE);
            holder.binding.txtBot.setText(model.getTxtBot());
        }
        if (holder.binding.llUser.getVisibility() == View.VISIBLE) {
            holder.binding.txtUserName.setText(username.charAt(0) + "");
            holder.binding.txtUserMsg.setText(model.getTxtUser());
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
