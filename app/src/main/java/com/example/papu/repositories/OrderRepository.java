package com.example.papu.repositories;

import com.example.papu.core.Order;
import com.example.papu.core.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class OrderRepository {
    private final DatabaseReference databaseReference =
            FirebaseDatabase.getInstance("https://papu-c09ca-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private final UserRepository userRepository = new UserRepository();

    public void submitOrder(Order order) {
        databaseReference.child("orders")
                .child(UUID.randomUUID().toString())
                .setValue(order);
    }

    public void getOrders(OnCompleteListener listener) {
        databaseReference.child("orders")
                .get()
                .addOnCompleteListener(listener);
    }
}
