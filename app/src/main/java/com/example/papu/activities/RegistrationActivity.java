package com.example.papu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.papu.R;
import com.example.papu.core.Role;
import com.example.papu.core.User;
import com.example.papu.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

import static com.example.papu.repositories.UserRepository.parseEmail;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView, companyTextView;
    private Button btn;
    private ProgressBar progressbar;
    private UserRepository userRepository;
    private Spinner roleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        roleSpinner = (Spinner) findViewById(R.id.role);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,  R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        userRepository = new UserRepository();
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        companyTextView = findViewById(R.id.companyName);

        btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);

        findViewById(R.id.toLogin).setOnClickListener( v ->
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class))
        );

        btn.setOnClickListener(view -> registerNewUser());
    }

    private void registerNewUser() {
        progressbar.setVisibility(View.VISIBLE);
        String email = emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        String company = companyTextView.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();


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
        User newUser = User.builder()
                .email(email)
                .username(parseEmail(email))
                .company(company)
                .role(UserRepository.getUserRole(role))
                .build();

        OnCompleteListener listener = (task) -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(),
                        "Registration successful!",
                        Toast.LENGTH_LONG)
                        .show();
                findViewById(R.id.progressbar).setVisibility(View.GONE);

                Intent intent = new Intent(
                    RegistrationActivity.this,
                    LoginActivity.class
                );

                startActivity(intent);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Registration failed!!"
                                + " Please try again later",
                        Toast.LENGTH_LONG)
                        .show();

                findViewById(R.id.progressbar).setVisibility(View.GONE);
            }
        };
        userRepository.registerUser(newUser, password, listener);
    }
}