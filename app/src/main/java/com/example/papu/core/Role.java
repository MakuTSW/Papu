package com.example.papu.core;

import com.example.papu.activities.CourierActivity;
import com.example.papu.activities.CustomerActivity;
import com.example.papu.activities.restaurant.RestaurantActivity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    RESTAURANT(RestaurantActivity.class),
    COURIER(CourierActivity.class),
    CUSTOMER(CustomerActivity.class);
    Class appCompatActivity;
}
