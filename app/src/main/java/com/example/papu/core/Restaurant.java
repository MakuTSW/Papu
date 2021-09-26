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
public class Restaurant {
    private String username;
    private String city;
    private String street;
    private String number;
    private String name;
    private List<Meal> meals;
}
