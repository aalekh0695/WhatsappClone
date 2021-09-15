package com.example.whatsappclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsappclone.R;
import com.example.whatsappclone.adapters.UserAdapter;
import com.example.whatsappclone.databinding.FragmentChatsBinding;
import com.example.whatsappclone.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";

    FragmentChatsBinding binding;
    List<User> userList;
    FirebaseDatabase firebaseDatabase;

    public ChatsFragment() {
        // Required empty public constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        UserAdapter userAdapter = new UserAdapter(userList, getContext(), getActivity());
        binding.chatFragRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.chatFragRecyclerView.setAdapter(userAdapter);

        Log.d(TAG, "onCreateView: calling db");

        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(Objects.equals(dataSnapshot.getKey(), FirebaseAuth.getInstance().getUid())) {
                        continue;
                    }

                    User user = dataSnapshot.getValue(User.class);
                    user.setUserId(dataSnapshot.getKey());
                    userList.add(user);
                    Log.d(TAG, "onDataChange: "+user.toString());
                }

                Log.d(TAG, "onDataChange: list size = "+userList.size());

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return binding.getRoot();
    }
}