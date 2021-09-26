package com.example.papu.activities.customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.papu.R;
import com.example.papu.core.Customer;
import com.example.papu.core.Restaurant;
import com.example.papu.core.User;
import com.example.papu.repositories.RestaurantRepository;
import com.example.papu.repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;

public class CustomerAddressActivity extends AppCompatActivity {
    Button submit;
    EditText phoneNumber, city, street, number;
    UserRepository userRepository = new UserRepository();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_address);
        submit = findViewById(R.id.CustomerSubmitAddress);
        phoneNumber = findViewById(R.id.CustomerPhoneNumber);
        city = findViewById(R.id.CustomerCity);
        street = findViewById(R.id.CustomerStreet);
        number = findViewById(R.id.CustomerNumber);

        OnCompleteListener<DataSnapshot> setCustomerData = (e) -> {
            if (e.isSuccessful()) {
                Customer customer = e.getResult().getValue(Customer.class);
                phoneNumber.setText(customer.getPhone());
                city.setText(customer.getCity());
                street.setText(customer.getStreet());
                number.setText(customer.getNumber());
            }
        };
        userRepository.getCurrentCustomer(setCustomerData);

        submit.setOnClickListener((e) -> {
            userRepository.saveCustomerData(
                    phoneNumber.getText().toString(),
                    city.getText().toString(),
                    street.getText().toString(),
                    number.getText().toString()
            );
            finish();
        });
    }
}
