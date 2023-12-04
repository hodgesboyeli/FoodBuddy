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
    public int getZipCode() {return zipCode;}
    public void clearRatingValues(){
        ratingSum = rating;
        ratingCount = 1;
    }


    public Restaurants(String id, String name, float rating, int ratingCount, float ratingSum, int zipCode, String address, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.ratingSum = ratingSum;
        this.zipCode = zipCode;
        this.address = address;
        this.isFavorite = isFavorite;
    }
}
