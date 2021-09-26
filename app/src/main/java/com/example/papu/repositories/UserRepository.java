package com.example.papu.repositories;

import com.example.papu.core.Courier;
import com.example.papu.core.Customer;
import com.example.papu.core.Restaurant;
import com.example.papu.core.Role;
import com.example.papu.core.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private final DatabaseReference databaseReference =
            FirebaseDatabase.getInstance("https://papu-c09ca-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
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
        Role role = user.getRole();
        if (role.equals(Role.COURIER)) {
            Courier courier = Courier.builder()
                    .username(user.getUsername())
                    .build();
            databaseReference.child("couriers")
                    .child(user.getUsername())
                    .setValue(courier);
        } else if (role.equals(Role.CUSTOMER)) {
            Customer customer = Customer.builder()
                    .username(user.getUsername())
                    .build();
            databaseReference.child("customers")
                    .child(user.getUsername())
                    .setValue(customer);
        } else if (role.equals(Role.RESTAURANT)) {
            Restaurant restaurant = Restaurant.builder()
                    .username(user.getUsername())
                    .build();
            databaseReference.child("restaurants")
                    .child(user.getUsername())
                    .setValue(restaurant);
        }
    }

    public void getCurrentUser(OnCompleteListener onCompleteListener) {
        String email = firebaseAuth.getCurrentUser().getEmail();
        databaseReference.child("users")
                .child(parseEmail(email))
                .get()
                .addOnCompleteListener(onCompleteListener);
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

    public static Role getUserRole(String role){
        if (Role.RESTAURANT.toString().equals(role.toUpperCase())) {
            return Role.RESTAURANT;
        } else if (Role.COURIER.toString().equals(role.toUpperCase())) {
            return Role.COURIER;
        } else if (Role.CUSTOMER.toString().equals(role.toUpperCase())) {
            return Role.CUSTOMER;
        } else throw new IllegalStateException("Incorrect role");
    }
}
