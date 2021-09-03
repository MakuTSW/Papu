package com.example.papu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);

        findViewById(R.id.toLogin).setOnClickListener( v ->
                startActivity(new Intent(MainActivity.this, LoginActivity.class))
        );

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {

        progressbar.setVisibility(View.VISIBLE);

        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        EditText companyName = findViewById(R.id.companyName);
                        writeNewUser(parseEmail(email), email, companyName.getText().toString());

                        Toast.makeText(getApplicationContext(),
                                "Registration successful!",
                                Toast.LENGTH_LONG)
                                .show();

                        findViewById(R.id.progressbar).setVisibility(View.GONE);

                        Intent intent
                                = new Intent(MainActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                    }
                    else {

                        Toast.makeText(
                                getApplicationContext(),
                                "Registration failed!!"
                                        + " Please try again later",
                                Toast.LENGTH_LONG)
                                .show();

                        findViewById(R.id.progressbar).setVisibility(View.GONE);
                    }
                });
    }

    private String parseEmail(String email) {
        String maliToUrl = "";
        for(int i=0; i<email.length(); i++) {
            if(email.charAt(i) != '@' && email.charAt(i) != '.')
                maliToUrl+=email.charAt(i);
        }
        return maliToUrl;
    }

    public void writeNewUser(String name, String email, String company) {
        User user = new User(name, email, company);
        System.out.println(name + " " + email + " " + company);
        mDatabase.child("users").child(name).setValue(user);
    }
}
