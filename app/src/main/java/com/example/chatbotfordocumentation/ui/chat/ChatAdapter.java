package com.example.chatbotfordocumentation.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbotfordocumentation.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages){
        this.chatMessages = chatMessages;
    }

    @Override
    public int getItemViewType(int position){
        return chatMessages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType == ChatMessage.TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        ChatMessage chatMessage = chatMessages.get(position);
        if(holder.getItemViewType() == ChatMessage.TYPE_SENT){
            ((SentMessageHolder) holder).bind(chatMessage);
        } else {
            ((ReceivedMessageHolder) holder).bind(chatMessage);
        }
    }

    @Override
    public int getItemCount(){
        return chatMessages.size();
    }

    // ViewHolder для отправленных сообщений
    public class SentMessageHolder extends RecyclerView.ViewHolder{
        TextView textViewMessage;

        SentMessageHolder(View itemView){
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.text_message_body);
        }

        void bind(ChatMessage chatMessage){
            textViewMessage.setText(chatMessage.getMessage());
        }
    }

    // ViewHolder для полученных сообщений
    public class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView textViewMessage;

        ReceivedMessageHolder(View itemView){
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.text_message_body);
        }

        void bind(ChatMessage chatMessage){
            textViewMessage.setText(chatMessage.getMessage());
        }
    }
}
