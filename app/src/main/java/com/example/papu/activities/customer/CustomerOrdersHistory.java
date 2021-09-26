package com.example.papu.activities.customer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.core.Order;
import com.example.papu.core.Restaurant;
import com.example.papu.core.User;
import com.example.papu.repositories.OrderRepository;
import com.example.papu.repositories.RestaurantRepository;
import com.example.papu.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomerOrdersHistory extends AppCompatActivity {

    LinearLayout ordersList;
    UserRepository userRepository = new UserRepository();
    OrderRepository orderRepository = new OrderRepository();
    RestaurantRepository restaurantRepository = new RestaurantRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_history);
        ordersList = findViewById(R.id.CustomerOrdersHistoryList);

        StringBuilder username = new StringBuilder();
        OnCompleteListener<DataSnapshot> setUsernameListener = (e) -> {
            if (e.isSuccessful()) {
                User currentUser = e.getResult().getValue(User.class);
                username.append(currentUser.getUsername());
            }
        };
        userRepository.getCurrentUser(setUsernameListener);

        OnCompleteListener<DataSnapshot> setOrdersHistoryList = (e) -> {
            if (e.isSuccessful()) {
                List<Order> orders = new ArrayList<>();
                e.getResult().getChildren().forEach(snap -> orders.add(snap.getValue(Order.class)));
                List<Order> userOrders = userOrders = orders.stream()
                        .filter(order -> order.getCustomerUsername().equals(username.toString()))
                        .collect(Collectors.toList());

                userOrders.forEach(order -> {
                    StringBuilder restaurantName = new StringBuilder();
                    OnCompleteListener<DataSnapshot> setRestaurantNameListener = (f) -> {
                        if (f.isSuccessful()) {
                            restaurantName.append(f.getResult().getValue(Restaurant.class).getName());
                            Fragment newFragment = new CustomerHistoryEntryFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("name", restaurantName.toString());
                            bundle.putString("state", order.getState().toString().toLowerCase());

                            StringBuilder productsBuilder = new StringBuilder();
                            order.getOrderMeals().forEach(meal -> {
                                productsBuilder.append(meal.getName()).append(", ");
                            });
                            bundle.putString("products", productsBuilder.toString());
                            bundle.putDouble("totalPrice", order.getTotalPrice());
                            newFragment.setArguments(bundle);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.add(R.id.CustomerOrdersHistoryList, newFragment, UUID.randomUUID().toString()).commit();
                        }
                    };
                    restaurantRepository.getRestaurantByUsername(order.getRestaurantUsername(), setRestaurantNameListener);
                });
                }
        };

        orderRepository.getOrders(setOrdersHistoryList);
    }
}
