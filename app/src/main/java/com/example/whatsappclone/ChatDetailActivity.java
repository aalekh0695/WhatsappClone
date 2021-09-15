package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.adapters.ChatAdapter;
import com.example.whatsappclone.adapters.UserAdapter;
import com.example.whatsappclone.databinding.ActivityChatDetailBinding;
import com.example.whatsappclone.models.MessageModel;
import com.example.whatsappclone.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatDetailActivity extends AppCompatActivity {
    private static final String TAG = "ChatDetailActivity";

    ActivityChatDetailBinding binding;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    List<MessageModel> messageModelList;
    String senderRoom, receiverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final String senderId = mAuth.getUid();
        final String receiverId = getIntent().getStringExtra(UserAdapter.USER_ID);
        String receiverName = getIntent().getStringExtra(UserAdapter.USER_NAME);
        String profilePic = getIntent().getStringExtra(UserAdapter.PROFILE_PIC);

        binding.tvReceiverName.setText(receiverName);
        Picasso.get().load(profilePic).placeholder(R.drawable.ic_user).into(binding.receiverProfileImage);

        messageModelList = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModelList, this);
        binding.chatActivityRecyclerview.setAdapter(chatAdapter);
        binding.chatActivityRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        senderRoom = senderId + ":" + receiverId;
        receiverRoom = receiverId + ":" + senderId;

        firebaseDatabase.getReference().child("chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            MessageModel messageReceived = dataSnapshot.getValue(MessageModel.class);
                            messageModelList.add(messageReceived);
//                            Log.d(TAG, "onDataChange: "+messageReceived);
                        }

                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    public void gotoMainActivity(View view) {
        Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        /*Intent intent = getIntent();
        intent.putExtra("lastMessage", "stay calm");
        setResult(RESULT_OK, intent);
        finish();*/
    }

    public void sendMessage(View view) {
        String messageToBeSent = binding.etSendMessage.getText().toString();
        MessageModel message = new MessageModel(mAuth.getUid(), messageToBeSent, new Date().getTime());
        binding.etSendMessage.setText("");

        firebaseDatabase.getReference().child("chats")
                .child(senderRoom)
                .push()
                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseDatabase.getReference().child("chats")
                        .child(receiverRoom)
                        .push()
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                /*Toast.makeText(ChatDetailActivity.this,
                                        "message saved successfully", Toast.LENGTH_SHORT).show();*/
                            }
                        });
            }
        });


    }
}