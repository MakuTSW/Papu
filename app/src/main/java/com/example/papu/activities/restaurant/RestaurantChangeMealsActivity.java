package com.example.papu.activities.restaurant;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.core.Meal;
import com.example.papu.core.Restaurant;
import com.example.papu.repositories.RestaurantRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

public class RestaurantChangeMealsActivity extends AppCompatActivity {
    private final RestaurantRepository restaurantRepository = new RestaurantRepository();
    LinearLayout mealsScrollView;
    Button addNewMeal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_change_meals);
        mealsScrollView = findViewById(R.id.RestaurantMealsList);
        addNewMeal = findViewById(R.id.RestaurantAddMeal);

        OnCompleteListener<DataSnapshot> setMealsListener = (e) -> {
            if (e.isSuccessful()) {
                Restaurant restaurant = e.getResult().getValue(Restaurant.class);
                if (restaurant.getMeals() == null) return;
                for (Meal meal : restaurant.getMeals()) {
                    Fragment newFragment = new MealEditFragment();
                    Bundle bundle = new Bundle();
                    bundle.putDouble("price", meal.getPrice());
                    bundle.putString("name", meal.getName());
                    bundle.putString("ingredients", meal.getIngredients());
                    newFragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.RestaurantMealsList, newFragment, meal.getName()).commit();
                }
            }
        };

        addNewMeal.setOnClickListener(e -> {
            startActivity(new Intent(RestaurantChangeMealsActivity.this, AddMealActivity.class));
        });

        restaurantRepository.getCurrentRestaurant(setMealsListener);
    }
}
