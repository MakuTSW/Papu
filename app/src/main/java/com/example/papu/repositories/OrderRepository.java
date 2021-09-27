package com.example.papu.repositories;

import com.example.papu.core.Order;
import com.example.papu.core.OrderState;
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
    public void updateState(String uuid, boolean next) {
        databaseReference.child("orders")
                .child(uuid)
                .get()
                .addOnCompleteListener(e -> {
                    if (e.isSuccessful()) {
                        Order order = e.getResult().getValue(Order.class);
                        OrderState state = order.getState();
                        if (next) {
                            order.setState(state.getNext());
                        } else {
                            order.setState(state.getPrev());
                        }
                        databaseReference.child("orders")
                                .child(uuid)
                                .setValue(order);
                    }
                });
    }
}
