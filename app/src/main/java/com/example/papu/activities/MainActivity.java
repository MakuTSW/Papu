package com.example.papu.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.papu.R;
import com.example.papu.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OnCompleteListener autoLoginSuccessListener = (task) -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            } else {
                Toast.makeText(getApplicationContext(),
                        "Auto-login failed, please provide login credentials!",
                        Toast.LENGTH_LONG)
                        .show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            finish();
        };

        //fancy welcome screen with dummy progressBar
        new Handler().postDelayed(() -> {
            UserRepository userRepository = new UserRepository();
            SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
            if (sp.contains("email") && sp.contains("password")) {
                userRepository.loginUser(
                        sp.getString("email", "error"),
                        sp.getString("password", "error"),
                        autoLoginSuccessListener
                );
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}
