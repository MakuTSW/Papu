package com.example.papu.activities.restaurant;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.activities.customer.CustomerHistoryEntryFragment;
import com.example.papu.core.Customer;
import com.example.papu.core.Order;
import com.example.papu.core.OrderState;
import com.example.papu.core.Restaurant;
import com.example.papu.core.User;
import com.example.papu.repositories.OrderRepository;
import com.example.papu.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RestaurantOrderHistoryActivity extends AppCompatActivity {
    LinearLayout ordersHistory;
    TextView header;
    OrderRepository orderRepository = new OrderRepository();
    UserRepository userRepository = new UserRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_order_history);
        ordersHistory = findViewById(R.id.RestaurantOrdersHistoryList);
        header = findViewById(R.id.RestaurantOrdersHeader);

        boolean isActive = getIntent().getExtras().getBoolean("active");
        String headerText = isActive ? "Active orders" : "Orders history";
        header.setText(headerText);

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
                List<String> uuids = new ArrayList<>();
                e.getResult().getChildren().forEach(snap -> {
                    orders.add(snap.getValue(Order.class));
                    uuids.add(snap.getKey());
                });
                List<Order> restaurantOrders = orders.stream()
                        .filter(order -> order.getRestaurantUsername().equals(username.toString()))
                        .filter(order -> {
                            if (isActive) {
                                return !order.getState().equals(OrderState.COMPLETED);
                            } else {
                                return order.getState().equals(OrderState.COMPLETED);
                            }
                        })
                        .collect(Collectors.toList());

                restaurantOrders.forEach(order -> {
                    StringBuilder customerData = new StringBuilder();
                    OnCompleteListener<DataSnapshot> setCustomerDataForOrderListener = (f) -> {
                        if (f.isSuccessful()) {
                            Customer customer = f.getResult().getValue(Customer.class);
                            customerData.append("Cell - ")
                                    .append(customer.getPhone())
                                    .append("; Address - ")
                                    .append(customer.getCity())
                                    .append(", ")
                                    .append(customer.getStreet())
                                    .append(" ")
                                    .append(customer.getNumber());
                            RestaurantHistoryEntryFragment newFragment = new RestaurantHistoryEntryFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("customerData", customerData.toString());
                            bundle.putString("state", order.getState().toString().toLowerCase());

                            StringBuilder productsBuilder = new StringBuilder();
                            order.getOrderMeals().forEach(meal -> {
                                productsBuilder.append(meal.getName()).append(", ");
                            });
                            bundle.putString("products", productsBuilder.toString());
                            bundle.putDouble("totalPrice", order.getTotalPrice());
                            bundle.putString("uuid",  uuids.get(orders.indexOf(order)));

                            newFragment.setArguments(bundle);
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.add(R.id.RestaurantOrdersHistoryList, newFragment, UUID.randomUUID().toString()).commit();
                        }
                    };
                    userRepository.getCustomerByUsername(order.getCustomerUsername(), setCustomerDataForOrderListener);
                });
            }
        };

        orderRepository.getOrders(setOrdersHistoryList);



    }
}
