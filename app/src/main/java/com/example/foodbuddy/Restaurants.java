package com.example.foodbuddy;

import lombok.Data;

@Data
public class Restaurants {
    private String name;
    private float rating;
    private int zipCode;

    private String address;
    private boolean isFavorite;


    public Restaurants() {

    }
    public boolean getIsFavorite() {
        return isFavorite;
    }
    public Restaurants(String name, float rating, int zipCode, String address, boolean isFavorite) {
        this.name = name;
        this.rating = rating;
        this.zipCode = zipCode;
        this.address = address;
        this.isFavorite = isFavorite;
    }
}
