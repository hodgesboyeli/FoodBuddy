package com.example.foodbuddy;

import java.io.Serializable;

import lombok.Data;

@Data
public class Restaurants implements Serializable {
    private String id;
    private String name;
    private float rating;
    private float ratingSum = this.rating;
    private int ratingCount = 1;
    private int zipCode;
    private String address;
    private String city;
    private String state;

    private boolean isFavorite;


    public Restaurants() {

    }
    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {return name;}
    public Float getRating() {return rating;}
    public void incrementRatingCount(){ratingCount++;}
    public void updateRatingSum (Float newRating){ratingSum += newRating;}
    public void calculateRating(){
        rating = ratingSum/ratingCount;
        rating = (float) (Math.round(rating * 10.0) / 10.0);
    }
    public int getRatingCount(){return ratingCount;}
    public float getRatingSum(){return ratingSum;}
    public String getAddress() {return address;}
    public String getCity() {return city;}
    public String getState() {return state;}

    public int getZipCode() {return zipCode;}
    public void clearRatingValues(){
        ratingSum = rating;
        ratingCount = 1;
    }

    public Restaurants(String id, String name, float rating, float ratingSum, int ratingCount, int zipCode, String address, String city, String state, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.ratingSum = ratingSum;
        this.ratingCount = ratingCount;
        this.zipCode = zipCode;
        this.address = address;
        this.city = city;
        this.state = state;
        this.isFavorite = isFavorite;
    }
}
