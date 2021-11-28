package com.example.supermarket.model;

import lombok.Data;

@Data
public class User {
    private int id;
    private String name;

    public User(int id) {
        this.id = id;
        this.name = "user-" + id;
    }
}