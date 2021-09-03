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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.passwd);
        Btn = findViewById(R.id.btnregister);
        progressbar = findViewById(R.id.progressbar);

        findViewById(R.id.toLogin).setOnClickListener( v ->
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class))
        );

        // Set on Click Listener on Registration button
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
                        try {
                            URL url = new URL("https://papu-c09ca-default-rtdb.europe-west1.firebasedatabase.app/users.json");
                            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                            httpCon.setDoOutput(true);
                            httpCon.setRequestMethod("PUT");
                            httpCon.connect();
                            OutputStreamWriter out = new OutputStreamWriter(
                                    httpCon.getOutputStream());
                            EditText companyName = findViewById(R.id.companyName);
                            out.write("\"" + parseEmail(email) + "\": {\n\"company\": \"" + companyName.getText().toString() + "\"\n}");
                            out.close();
                            Log.i("restApi", Integer.toString(httpCon.getResponseCode()));
                            Log.i("restApi", httpCon.getResponseMessage());
                            Toast.makeText(getApplicationContext(),
                                    httpCon.getResponseCode() + httpCon.getResponseMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                            Log.i("restApi", "done");
                        }catch (Exception exception){
                            Toast.makeText(getApplicationContext(),
                                    exception.toString(),
                                    Toast.LENGTH_LONG)
                                    .show();
                            Log.e("restApi", "undone");
                            exception.printStackTrace();
                        }

                        Toast.makeText(getApplicationContext(),
                                "Registration successful!",
                                Toast.LENGTH_LONG)
                                .show();

                        findViewById(R.id.progressbar).setVisibility(View.GONE);

                        Intent intent
                                = new Intent(RegistrationActivity.this,
                                MainActivity.class);
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
            if(email.charAt(i) != '@' || email.charAt(i) != '.')
                maliToUrl+=email.charAt(i);
        }
        return maliToUrl;
    }
}