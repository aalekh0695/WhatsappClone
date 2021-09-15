package com.example.whatsappclone.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;
import com.example.whatsappclone.models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {
    List<MessageModel> messageModelList;
    Context context;
    private final int SENDER_VIEW_TYPE = 1;
    private final int RECEIVER_VIEW_TYPE = 2;


    public ChatAdapter(List<MessageModel> messageModelList, Context context) {
        this.messageModelList = messageModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == SENDER_VIEW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        }

        view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
        return new ReceiverViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        }

        return RECEIVER_VIEW_TYPE;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messageModelList.get(position);

        if(holder instanceof SenderViewHolder) {
            ((SenderViewHolder) holder).tvSenderMessage.setText(message.getMessageText());
            //((SenderViewHolder) holder).tvSenderMessageTime.setText(message.getMessageTimeStamp().toString());
        } else if(holder instanceof ReceiverViewHolder) {
            ((ReceiverViewHolder) holder).tvReceiverMessage.setText(message.getMessageText());
        }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView tvReceiverMessage, tvReceiverMessageTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReceiverMessage = itemView.findViewById(R.id.tv_receiver_message);
            tvReceiverMessageTime = itemView.findViewById(R.id.tv_receiver_time);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView tvSenderMessage, tvSenderMessageTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSenderMessage = itemView.findViewById(R.id.tv_sender_message);
            tvSenderMessageTime = itemView.findViewById(R.id.tv_sender_time);
        }
    }
}
