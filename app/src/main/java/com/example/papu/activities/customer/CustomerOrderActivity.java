package com.example.papu.activities.customer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.activities.restaurant.MealEditFragment;
import com.example.papu.core.Meal;
import com.example.papu.core.Order;
import com.example.papu.core.OrderState;
import com.example.papu.core.Restaurant;
import com.example.papu.core.User;
import com.example.papu.repositories.OrderRepository;
import com.example.papu.repositories.RestaurantRepository;
import com.example.papu.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class CustomerOrderActivity extends AppCompatActivity {

    Button summarize;
    TextView restaurantName;
    LinearLayout mealsScrollView;
    RestaurantRepository restaurantRepository = new RestaurantRepository();
    UserRepository userRepository = new UserRepository();
    OrderRepository orderRepository = new OrderRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        summarize = findViewById(R.id.SummarizeOrder);
        restaurantName = findViewById(R.id.OrderRestaurantName);
        String restaurantUsername = getIntent().getExtras().getString("restaurantUsername");
        StringBuilder username = new StringBuilder();
        OnCompleteListener<DataSnapshot> setUsernameListener = (e) -> {
            if (e.isSuccessful()) {
                User currentUser = e.getResult().getValue(User.class);
                username.append(currentUser.getUsername());
            }
        };
        userRepository.getCurrentUser(setUsernameListener);

        Order order = Order.builder()
                .restaurantUsername(restaurantUsername)
                .orderMeals(new ArrayList<>())
                .totalPrice(0)
                .state(OrderState.CREATED)
                .build();

        OnCompleteListener<DataSnapshot> setMealsListener = (e) -> {
            if (e.isSuccessful()) {
                Restaurant restaurant = e.getResult().getValue(Restaurant.class);
                restaurantName.setText(restaurant.getName());
                if (restaurant.getMeals() == null) return;
                for (Meal meal : restaurant.getMeals()) {
                    MealOrderFragment newFragment = new MealOrderFragment();
                    newFragment.setOnClickListener(t -> {
                        order.getOrderMeals().add(meal);
                        order.setTotalPrice(order.getTotalPrice() + meal.getPrice());
                    });
                    Bundle bundle = new Bundle();
                    bundle.putDouble("price", meal.getPrice());
                    bundle.putString("name", meal.getName());
                    bundle.putString("ingredients", meal.getIngredients());
                    newFragment.setArguments(bundle);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.add(R.id.OrderMealsList, newFragment, meal.getName()).commit();
                }
            }
        };

        restaurantRepository.getRestaurantByUsername(restaurantUsername, setMealsListener);

        summarize.setOnClickListener(e -> {
            order.setCustomerUsername(username.toString());
            orderRepository.submitOrder(order);
            Toast.makeText(getApplicationContext(),
                    "Order submitted successfully.",
                    Toast.LENGTH_LONG)
                    .show();
            finish();
        });
    }
}
