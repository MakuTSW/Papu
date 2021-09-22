package com.example.papu.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.core.Role;
import com.example.papu.core.User;
import com.example.papu.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button btn;
    private ProgressBar progressbar;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userRepository = new UserRepository();
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        btn = findViewById(R.id.login);
        progressbar = findViewById(R.id.progressBar);
        
        findViewById(R.id.toRegistration).setOnClickListener( v->
                startActivity(new Intent(LoginActivity.this,
                        RegistrationActivity.class))
        );

        btn.setOnClickListener(view -> loginUserAccount());
    }

    private void loginUserAccount() {
        progressbar.setVisibility(View.VISIBLE);
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                "Please enter email!!",
                Toast.LENGTH_LONG)
                .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                "Please enter password!!",
                Toast.LENGTH_LONG)
                .show();
            return;
        }

        OnCompleteListener<DataSnapshot> currentUserListener = (task) -> {
            if (task.isSuccessful()) {
                Role role = task.getResult().getValue(User.class).getRole();
                Intent intent = new Intent(LoginActivity.this, role.getAppCompatActivity());
                startActivity(intent);
            } else {
                Log.e("firebase", "Cannot get data");
            }
        };

        OnCompleteListener listener = (task) -> {
            if (task.isSuccessful()) {
                saveLoginCredentials(email, password);
                Toast.makeText(getApplicationContext(),
                        "Login successful!!",
                        Toast.LENGTH_LONG)
                        .show();
                findViewById(R.id.progressBar).setVisibility(View.GONE);

                userRepository.getCurrentUser(currentUserListener);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Login failed!!",
                        Toast.LENGTH_LONG)
                        .show();
                progressbar.setVisibility(View.GONE);
            }
        };


        userRepository.loginUser(email, password, listener);
    }

    private void saveLoginCredentials(String email, String password) {
        SharedPreferences sp = getSharedPreferences("credentials", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }
}