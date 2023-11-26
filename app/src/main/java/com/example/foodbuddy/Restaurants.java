package com.example.foodbuddy;

import lombok.Data;

@Data
public class Restaurants {
    private String name;
    private float rating;
    private int zipCode;

    private String address;

    public Restaurants() {

    }

    public Restaurants(String name, float rating, int zipCode, String address) {
        this.name = name;
        this.rating = rating;
        this.zipCode = zipCode;
        this.address = address;
    }
}
