package com.example.papu.activities.customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.core.Restaurant;
import com.example.papu.repositories.RestaurantRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomerActivity extends AppCompatActivity {

    Button historyOrders;
    Button setAddress;
    ListView restaurantsList;
    RestaurantRepository restaurantRepository = new RestaurantRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        historyOrders = findViewById(R.id.CustomerOrdersHistory);
        restaurantsList = findViewById(R.id.CustomerRestaurantsList);
        setAddress = findViewById(R.id.CustomerSetAddress);
        List<String> restaurantNames = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantNames);
        restaurantsList.setAdapter(adapter);
        List<Restaurant> restaurants = new ArrayList<>();

        OnCompleteListener<DataSnapshot> createListOfRestaurantsListener = (e) -> {
            if (e.isSuccessful()) {
                if(e.getResult().getChildrenCount() == 0) return;
                e.getResult().getChildren().forEach(snap -> {
                    Restaurant restaurant = snap.getValue(Restaurant.class);
                    restaurantNames.add(restaurant.getName() + " - " + restaurant.getCity() + ", " + restaurant.getStreet());
                    restaurants.add(snap.getValue(Restaurant.class));
                });
                adapter.notifyDataSetChanged();
            }
        };

        restaurantRepository.getAllRestaurants(createListOfRestaurantsListener);
        restaurantsList.setOnItemClickListener((adapterView, view, e3, e4) -> {
            String clickedRestaurantName = ((TextView)view).getText().toString().split("-")[0].trim();
            Intent intent = new Intent(CustomerActivity.this, CustomerOrderActivity.class);
            Bundle b = new Bundle();
            Optional<Restaurant> clickedRestaurant = restaurants.stream().filter(restaurant -> {
                return restaurant.getName().equals(clickedRestaurantName);
            }).findFirst();
            if (!clickedRestaurant.isPresent()) return;
            b.putString("restaurantUsername", clickedRestaurant.get().getUsername());
            intent.putExtras(b);
            startActivity(intent);
        });

        historyOrders.setOnClickListener(e -> {
            startActivity(new Intent(CustomerActivity.this, CustomerOrdersHistory.class));
        });

        setAddress.setOnClickListener(e -> {
            startActivity(new Intent(CustomerActivity.this, CustomerAddressActivity.class));
        });
    }
}
