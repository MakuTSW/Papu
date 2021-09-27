package com.example.papu.activities.restaurant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.papu.R;
import com.example.papu.core.Meal;
import com.example.papu.repositories.RestaurantRepository;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MealEditFragment extends Fragment {
    Button delete;
    TextView ingredients;
    TextView name;
    View.OnClickListener listener;
    RestaurantRepository repository = new RestaurantRepository();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_edit_fragment,
                container, false);

        delete = view.findViewById(R.id.DeleteMeal);
        ingredients = view.findViewById(R.id.Ingredients);
        name = view.findViewById(R.id.MealName);

        ingredients.setText(ingredients.getText().toString() + " " + this.getArguments().getString("ingredients"));
        name.setText(name.getText().toString() + " " + this.getArguments().getString("name"));
        delete.setOnClickListener(e -> {
            repository.removeMeal(this.getArguments().getString("name"));
            Toast.makeText(getContext(),
                    "Meal removed successfully.",
                    Toast.LENGTH_LONG)
                    .show();
        });

        return view;
    }
}
