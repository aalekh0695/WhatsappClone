package com.example.whatsappclone.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.ChatDetailActivity;
import com.example.whatsappclone.R;
import com.example.whatsappclone.models.MessageModel;
import com.example.whatsappclone.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder>{
    private static final String TAG = "UserAdapter";

    List<User> userList;
    Context context;
    Activity activity;
    FirebaseDatabase firebaseDatabase;
    public static final String USER_NAME = "userName";
    public static final String USER_ID = "userId";
    public static final String PROFILE_PIC = "profilePic";

    public UserAdapter(List<User> userList, Context context, Activity activity) {
        this.userList = userList;
        this.context = context;
        this.activity = activity;
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_single_row, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        User user = userList.get(position);
        holder.tvConnectionName.setText(user.getUsername());
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.ic_user).into(holder.imageView);

        String senderRoom = FirebaseAuth.getInstance().getUid()+ ":" + user.getUserId();
        firebaseDatabase.getReference().child("chats").child(senderRoom)
                .orderByChild("messageTimeStamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()) {
                            for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                MessageModel messageReceived = dataSnapshot.getValue(MessageModel.class);
                                Log.d(TAG, "onDataChange: calling last message Node"+messageReceived);
                                holder.tvConnectionLastMessage
                                        .setText(dataSnapshot.child("messageText").getValue(String.class));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra(USER_NAME, user.getUsername());
                intent.putExtra(USER_ID, user.getUserId());
                intent.putExtra(PROFILE_PIC, user.getProfilePic());

                context.startActivity(intent);
//                activity.startActivityForResult(intent, 101);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView tvConnectionName, tvConnectionLastMessage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.receiver_profile_image);
            tvConnectionName = itemView.findViewById(R.id.tv_connection_name);
            tvConnectionLastMessage = itemView.findViewById(R.id.tv_connection_last_message);

        }
    }

}
