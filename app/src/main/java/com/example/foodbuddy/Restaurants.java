package com.example.foodbuddy;

import lombok.Data;

@Data
public class Restaurants {
    private String name;
    private float rating;
    private int zipCode;

    private String address;
    private boolean isFavortite;


    public Restaurants() {

    }
    public boolean getIsFavorite() {
        return isFavortite;
    }
    public Restaurants(String name, float rating, int zipCode, String address, boolean isFavortite) {
        this.name = name;
        this.rating = rating;
        this.zipCode = zipCode;
        this.address = address;
        this.isFavortite = isFavortite;
    }
}
