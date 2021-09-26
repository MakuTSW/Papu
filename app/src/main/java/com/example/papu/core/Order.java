package com.example.papu.core;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private String customerUsername;
    private String restaurantUsername;
    private List<Meal> orderMeals;
    private OrderState state;
    private double totalPrice;
}
