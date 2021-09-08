package com.example.papu.repositories;

import android.content.SharedPreferences;

import com.example.papu.activities.MainActivity;
import com.example.papu.core.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static String parseEmail(String email) {
        String maliToUrl = "";
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) != '@' && email.charAt(i) != '.')
                maliToUrl += email.charAt(i);
        }
        return maliToUrl;
    }

    public void createUser(User user) {
        databaseReference.child("users")
                .child(user.getUsername())
                .setValue(user);
    }

    public User getCurrentUser() {
        String email = firebaseAuth.getCurrentUser().getEmail();
        return null;
    }

    public void registerUser(User user, String password, OnCompleteListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnSuccessListener(result -> createUser(user))
                .addOnCompleteListener(listener);
    }

    public void loginUser(String email, String password, OnCompleteListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(listener);

    }
}
