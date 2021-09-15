package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.adapters.ChatAdapter;
import com.example.whatsappclone.databinding.ActivityGroupChatBinding;
import com.example.whatsappclone.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {
    private static final String TAG = "GroupChatActivity";

    ActivityGroupChatBinding binding;
    FirebaseDatabase firebaseDatabase;
    private final String SENDER_ID = FirebaseAuth.getInstance().getUid();
    private static final String GROUP_CHAT_ID = "group_chat_1";
    List<MessageModel> messageModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        firebaseDatabase = FirebaseDatabase.getInstance();
        messageModelList = new ArrayList<>();

        ChatAdapter chatAdapter = new ChatAdapter(messageModelList, this);
        binding.groupChatActivityRecyclerview.setAdapter(chatAdapter);
        binding.groupChatActivityRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase.getReference().child("group_chats").child(GROUP_CHAT_ID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModelList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel messageReceived = dataSnapshot.getValue(MessageModel.class);
                            messageModelList.add(messageReceived);
                            Log.d(TAG, "onDataChange: " + messageReceived);
                        }

                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void sendMessageInGroup(View view) {
        String messageToSend = binding.etGroupSendMessage.getText().toString();
        MessageModel message = new MessageModel(SENDER_ID, messageToSend, new Date().getTime());
        binding.etGroupSendMessage.setText("");

        firebaseDatabase.getReference().child("group_chats").child(GROUP_CHAT_ID)
                .push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: message saved successfully");
                Toast.makeText(GroupChatActivity.this,
                        "message sent successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void gotoMainActivity(View view) {
        Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
        startActivity(intent);
    }
}