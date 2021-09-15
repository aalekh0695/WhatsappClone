package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsappclone.databinding.ActivitySignupBinding;
import com.example.whatsappclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.security.ProtectionDomain;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Signing up...");

        binding.btnSignup.setOnClickListener(v -> {
            progressDialog.show();
            String email = binding.etSignupEmail.getText().toString();
            String password = binding.etSignupPassword.getText().toString();
            String username = binding.etSignupName.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if(task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();

                                String uid = task.getResult().getUser().getUid();
                                User user = new User(username, email, password);

                                firebaseDatabase.getReference().child("Users").child(uid).setValue(user);

                            } else {
                                Toast.makeText(SignupActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: "+task.getException().getMessage());
                            }

                        }
                    });
        });


    }

    public void gotoSigninActivity(View view) {
        Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }
}