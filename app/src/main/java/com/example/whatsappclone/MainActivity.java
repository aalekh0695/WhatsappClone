package com.example.whatsappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.adapters.FragmentAdapter;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 101 && RESULT_OK == resultCode) {
                String lastMessage = data.getStringExtra("lastMessage");
                Toast.makeText(this, "lastMessage : "+lastMessage, Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Toast.makeText(this, "lastMessage exception : "+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_chat:
                gotoGroupChatActivity();
                break;

            case R.id.settings :
//                Toast.makeText(MainActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();
                gotoSettingsActivity();
                break;

            case R.id.logout :
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Signing out!!", Toast.LENGTH_SHORT).show();
                gotoSigninActivity();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void gotoSigninActivity() {
        Intent intent = new Intent(MainActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoGroupChatActivity() {
        Intent intent = new Intent(MainActivity.this, GroupChatActivity.class);
        startActivity(intent);
        finish();
    }
}