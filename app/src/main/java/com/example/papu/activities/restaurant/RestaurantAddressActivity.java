package com.example.papu.activities.restaurant;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.core.Restaurant;
import com.example.papu.repositories.RestaurantRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

public class RestaurantAddressActivity extends AppCompatActivity {
    Button submit;
    EditText name, city, street, number;
    RestaurantRepository restaurantRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_address);
        submit = findViewById(R.id.SubmitAddress);
        name = findViewById(R.id.RestaurantName);
        city = findViewById(R.id.RestaurantCity);
        street = findViewById(R.id.RestaurantStreet);
        number = findViewById(R.id.RestaurantNumber);
        restaurantRepository = new RestaurantRepository();

        OnCompleteListener<DataSnapshot> setRestaurantData = (e) -> {
            if (e.isSuccessful()) {
                Restaurant restaurant = e.getResult().getValue(Restaurant.class);
                name.setText(restaurant.getName());
                city.setText(restaurant.getCity());
                street.setText(restaurant.getStreet());
                number.setText(restaurant.getNumber());
            }
        };
        restaurantRepository.getCurrentRestaurant(setRestaurantData);

        submit.setOnClickListener((e) -> {
            restaurantRepository.saveRestaurantData(
                    name.getText().toString(),
                    city.getText().toString(),
                    street.getText().toString(),
                    number.getText().toString()
            );
            finish();
        });
    }
}
