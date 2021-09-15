package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.databinding.ActivitySettingsBinding;
import com.example.whatsappclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    ActivitySettingsBinding binding;
    public static final int GET_PROFILE_PIC_REQUEST_CODE = 102;

    FirebaseStorage firebaseStorage;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase.getReference().child("Users").child(mAuth.getUid())
                .child("profilePic").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String firebaseProfileImagePath = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "onCreate: firebaseProfileImagePath = "+ firebaseProfileImagePath);
                        Picasso.get().load(firebaseProfileImagePath)
                                .placeholder(R.drawable.ic_baseline_add_a_photo_24)
                                .into(binding.profileImage);
                    }
                });

        updateUI();

    }

    private void updateUI() {
        firebaseDatabase.getReference().child("Users").child(mAuth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d(TAG, "onSuccess: updateUI "+user);

                binding.etUpdateAbout.setText(user.getAbout());
                binding.etUpdateUsername.setText(user.getUsername());
            }
        });
    }

    public void gotoMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void updateProfilePic(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GET_PROFILE_PIC_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_PROFILE_PIC_REQUEST_CODE && data != null) {
            Uri imageUri = data.getData();
            binding.profileImage.setImageURI(imageUri);

            StorageReference storageReference = firebaseStorage.getReference().child("profile_pictures").child(mAuth.getUid());

            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(SettingsActivity.this,
                                    "pic uploaded", Toast.LENGTH_SHORT).show();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    DatabaseReference dbReference = firebaseDatabase.getReference()
                                            .child("Users").child(mAuth.getUid()).child("profilePic");

                                    dbReference.setValue(uri.toString());
                                }
                            });
                        }
                    });

        }

    }

    public void updateProfile(View view) {
        String username = binding.etUpdateUsername.getText().toString();
        String about = binding.etUpdateAbout.getText().toString();

        firebaseDatabase.getReference().child("Users").child(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Log.d(TAG, "onSuccess: updateProfile "+user);

                        user.setUsername(username);
                        user.setAbout(about);

                        firebaseDatabase.getReference().child("Users").child(mAuth.getUid())
                                .setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SettingsActivity.this,
                                                "Profile updated", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

    }
}