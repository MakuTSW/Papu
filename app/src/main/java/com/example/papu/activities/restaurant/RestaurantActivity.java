package com.example.papu.activities.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.papu.R;

public class RestaurantActivity extends AppCompatActivity {
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
            startActivity(intent);
        });

        LinearLayout activeOrders = findViewById(R.id.activeOrders);
        activeOrders.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RestaurantActivity.this,
                    RestaurantActiveOrdersActivity.class
            );
            startActivity(intent);
        });
    }
}
