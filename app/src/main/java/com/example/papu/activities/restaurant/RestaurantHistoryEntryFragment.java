package com.example.papu.activities.restaurant;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.papu.R;
import com.example.papu.core.OrderState;
import com.example.papu.repositories.OrderRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RestaurantHistoryEntryFragment extends Fragment {
    TextView products;
    TextView customerAddress;
    TextView state;
    TextView total;
    Button prevState;
    Button nextState;
    DecimalFormat df = new DecimalFormat("#.##");
    OrderRepository orderRepository = new OrderRepository();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_placed_orders_fragments,
                container, false);

        products = view.findViewById(R.id.HistoryOrderProducts);
        customerAddress = view.findViewById(R.id.HistoryCustomerAddress);
        state = view.findViewById(R.id.HistoryOrderState);
        total = view.findViewById(R.id.HistoryOrderTotalPrice);
        prevState = view.findViewById(R.id.OrderPreviousState);
        nextState = view.findViewById(R.id.OrderNextState);
        List<OrderState> stateEnum = new ArrayList<>();
        stateEnum.add(OrderState.valueOf(this.getArguments().getString("state").toUpperCase()));
        String uuid = this.getArguments().getString("uuid");

        products.setText(products.getText().toString() + " " + this.getArguments().getString("products"));
        customerAddress.setText(customerAddress.getText().toString() + " " + this.getArguments().getString("customerData"));
        state.setText(state.getText().toString() + stateEnum.get(0).toString().toLowerCase());
        total.setText(total.getText().toString() + df.format(getArguments().getDouble("totalPrice")) + "zł");
        prevState.setOnClickListener(e -> {
            orderRepository.updateState(uuid, false);
            stateEnum.add(0, stateEnum.get(0).getPrev());
            state.setText("State: " + stateEnum.get(0).toString().toLowerCase());
        });
        nextState.setOnClickListener(e -> {
            orderRepository.updateState(uuid, true);
            stateEnum.add(0, stateEnum.get(0).getNext());
            state.setText("State: " + stateEnum.get(0).toString().toLowerCase());
        });
        return view;
    }


}
