package com.example.papu.activities.customer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.papu.R;
import com.example.papu.core.Meal;
import com.example.papu.repositories.RestaurantRepository;

import java.text.DecimalFormat;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class MealOrderFragment extends Fragment {
    Button addMeal;
    TextView ingredients;
    TextView name;
    View.OnClickListener mListener;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_order_fragment,
                container, false);

        addMeal = view.findViewById(R.id.AddMeal);
        ingredients = view.findViewById(R.id.Ingredients);
        name = view.findViewById(R.id.MealName);
        addMeal.setOnClickListener(mListener);
        ingredients.setText(ingredients.getText().toString() + " " + this.getArguments().getString("ingredients"));
        name.setText(name.getText().toString() + " " + this.getArguments().getString("name"));
        addMeal.setText(addMeal.getText().toString() + df.format(this.getArguments().getDouble("price")) + "zł");

        return view;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }
}
