package com.example.papu.repositories;

import com.example.papu.core.Meal;
import com.example.papu.core.Restaurant;
import com.example.papu.core.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantRepository {
    private final DatabaseReference databaseReference =
            FirebaseDatabase.getInstance("https://papu-c09ca-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    private final UserRepository userRepository = new UserRepository();

    public void getCurrentRestaurant(OnCompleteListener onCompleteListener) {
        OnCompleteListener<DataSnapshot> currentUserListener = (e) -> {
            if (e.isSuccessful()) {
                String username = e.getResult().getValue(User.class).getUsername();
                databaseReference.child("restaurants")
                        .child(username)
                        .get()
                        .addOnCompleteListener(onCompleteListener);
            }
        };
        userRepository.getCurrentUser(currentUserListener);
    }

    public void saveRestaurantData(String name, String city, String street, String number) {
        OnCompleteListener<DataSnapshot> updateRestaurantData = (e) -> {
            if (e.isSuccessful()) {
                Restaurant restaurant = e.getResult().getValue(Restaurant.class);
                restaurant.setName(name);
                restaurant.setCity(city);
                restaurant.setStreet(street);
                restaurant.setNumber(number);
                databaseReference.child("restaurants")
                        .child(restaurant.getUsername())
                        .setValue(restaurant);
            }
        };
        getCurrentRestaurant(updateRestaurantData);
    }

    public void removeMeal(String mealName) {
        OnCompleteListener<DataSnapshot> removeMealListener = (e) -> {
            if (e.isSuccessful()) {
                Restaurant restaurant = e.getResult().getValue(Restaurant.class);
                List<Meal> meals = restaurant.getMeals();
                if (meals == null) return;
                meals.removeIf(meal -> meal.getName().equals(mealName));
                restaurant.setMeals(meals);
                databaseReference.child("restaurants")
                        .child(restaurant.getUsername())
                        .setValue(restaurant);
            }
        };

        getCurrentRestaurant(removeMealListener);
    }

    public void addMeal(Meal meal) {
        OnCompleteListener<DataSnapshot> addMealListener = (e) -> {
            if (e.isSuccessful()) {
                Restaurant restaurant = e.getResult().getValue(Restaurant.class);
                List<Meal> meals = restaurant.getMeals();
                if (meals == null) meals = new ArrayList<>();
                meals.add(meal);
                restaurant.setMeals(meals);
                databaseReference.child("restaurants")
                        .child(restaurant.getUsername())
                        .setValue(restaurant);
            }
        };

        getCurrentRestaurant(addMealListener);
    }

}
