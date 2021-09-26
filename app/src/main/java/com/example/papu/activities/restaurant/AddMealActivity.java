package com.example.papu.activities.restaurant;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.core.Meal;
import com.example.papu.repositories.RestaurantRepository;

import java.math.BigDecimal;

public class AddMealActivity extends AppCompatActivity {

    EditText newMealName, newMealPrice, newMealIngredients;
    Button submit;
    RestaurantRepository restaurantRepository = new RestaurantRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_add_meal);
        newMealName = findViewById(R.id.NewMealName);
        newMealPrice = findViewById(R.id.NewMealPrice);
        newMealIngredients = findViewById(R.id.NewMealIngredients);
        submit = findViewById(R.id.NewMealSubmit);

        submit.setOnClickListener(e -> {
            String name = newMealName.getText().toString();
            String ingredients = newMealIngredients.getText().toString();
            String price = newMealPrice.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter valid name.",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(ingredients)) {
                Toast.makeText(getApplicationContext(),
                        "Please enter valid ingredients.",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            if (TextUtils.isEmpty(price) && !price.matches("\\d+\\.?\\d*")) {
                Toast.makeText(getApplicationContext(),
                        "Please enter valid ingredients.",
                        Toast.LENGTH_LONG)
                        .show();
                return;
            }

            double priceDouble = Double.parseDouble(price);
            Meal newMeal = Meal.builder()
                    .price(priceDouble)
                    .name(name)
                    .ingredients(ingredients)
                    .build();

            restaurantRepository.addMeal(newMeal);
            finish();
        });
    }
}
