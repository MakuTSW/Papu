package com.example.papu.activities.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.papu.R;
import com.example.papu.core.Order;
import com.example.papu.core.OrderState;
import com.example.papu.core.Restaurant;
import com.example.papu.repositories.OrderRepository;
import com.example.papu.repositories.RestaurantRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    RestaurantRepository restaurantRepository = new RestaurantRepository();
    OrderRepository orderRepository = new OrderRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        LinearLayout address = findViewById(R.id.restaurantAddress);
        address.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RestaurantActivity.this,
                    RestaurantAddressActivity.class
            );
            startActivity(intent);
        });

        LinearLayout changeMeals = findViewById(R.id.changeMeals);
        changeMeals.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RestaurantActivity.this,
                    RestaurantChangeMealsActivity.class
            );
            startActivity(intent);
        });

        LinearLayout orderHistory = findViewById(R.id.orderHistory);
        orderHistory.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RestaurantActivity.this,
                    RestaurantOrderHistoryActivity.class
            );
            Bundle bundle = new Bundle();
            bundle.putBoolean("active", false);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        LinearLayout activeOrders = findViewById(R.id.activeOrders);
        activeOrders.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RestaurantActivity.this,
                    RestaurantOrderHistoryActivity.class
            );
            Bundle bundle = new Bundle();
            bundle.putBoolean("active", true);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        TextView inProgressAmount = findViewById(R.id.inProgressMealsAmount);
        TextView completedAmount = findViewById(R.id.completedMealsAmount);



        OnCompleteListener<DataSnapshot> setActiveDataForRestaurantUsername = (e) -> {
            if (e.isSuccessful()) {
                Restaurant restaurant = e.getResult().getValue(Restaurant.class);

                OnCompleteListener<DataSnapshot> setActiveData = (f) -> {
                    if (f.isSuccessful()) {
                        List<Order> allOrders = new ArrayList<>();
                        f.getResult().getChildren().forEach(snap -> {
                            if (snap.getValue(Order.class).getRestaurantUsername().equals(restaurant.getUsername())){
                                allOrders.add(snap.getValue(Order.class));
                            }
                        });
                        long completed = allOrders.stream()
                                .filter(order -> order.getState().equals(OrderState.COMPLETED))
                                .count();
                        long inProgress = allOrders.stream()
                                .filter(order -> !order.getState().equals(OrderState.COMPLETED))
                                .count();
                        inProgressAmount.setText(Long.toString(inProgress));
                        completedAmount.setText(Long.toString(completed));
                    }
                };

                orderRepository.getOrders(setActiveData);
            }
        };
        restaurantRepository.getCurrentRestaurant(setActiveDataForRestaurantUsername);
    }
}
