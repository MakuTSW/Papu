package com.example.papu.activities.customer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.papu.R;

import java.text.DecimalFormat;

public class CustomerHistoryEntryFragment extends Fragment {
    TextView products;
    TextView name;
    TextView state;
    TextView total;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_history_entry,
                container, false);

        products = view.findViewById(R.id.CustomerOrderProducts);
        name = view.findViewById(R.id.CustomerOrderRestaurantName);
        state = view.findViewById(R.id.CustomerOrderState);
        total = view.findViewById(R.id.CustomerOrderTotalPrice);

        products.setText(products.getText().toString() + " " + this.getArguments().getString("products"));
        name.setText(name.getText().toString() + " " + this.getArguments().getString("name"));
        state.setText(state.getText().toString() + this.getArguments().getString("state"));
        total.setText(total.getText().toString() + df.format(getArguments().getDouble("totalPrice")) + "zł");

        return view;
    }
}
