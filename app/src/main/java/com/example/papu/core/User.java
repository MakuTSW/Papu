package com.example.papu.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class User {
    private String username;
    private String email;
    private String company;
    private Role role;
}