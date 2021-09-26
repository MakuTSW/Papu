package com.example.papu.core;

import com.example.papu.activities.customer.CustomerActivity;
import com.example.papu.activities.restaurant.RestaurantActivity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    RESTAURANT(RestaurantActivity.class),
    CUSTOMER(CustomerActivity.class);
    Class appCompatActivity;
}
