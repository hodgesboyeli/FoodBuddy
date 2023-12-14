package com.example.foodbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RestaurantListAdapter extends ArrayAdapter<Restaurants> {

    public RestaurantListAdapter(@NonNull Context context, @NonNull List<Restaurants> restaurantsList) {
        super(context, 0, restaurantsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_restaurant_item, parent, false);
        }

        Restaurants restaurant = getItem(position);

        if (restaurant != null) {
            TextView nameTextView = itemView.findViewById(R.id.nameTextView);
            TextView addressTextView = itemView.findViewById(R.id.addressTextView);
            //TextView cityTextView = itemView.findViewById(R.id.cityTextView);
            //TextView stateTextView = itemView.findViewById(R.id.stateTextView);

            TextView ratingTextView = itemView.findViewById(R.id.ratingTextView);

            // Set restaurant name
            nameTextView.setText(restaurant.getName());

            // Set restaurant address
            addressTextView.setText(restaurant.getAddress() + ", " + restaurant.getCity() + ", " + restaurant.getState() + ", " + restaurant.getZipCode());

            // Set restaurant rating
            ratingTextView.setText(String.valueOf(restaurant.getRating()));

            // Set restaurant logo based on the name
            ImageView logoImageView = itemView.findViewById(R.id.logoImageView);
            setRestaurantLogo(restaurant.getName(), logoImageView);
        }

        return itemView;
    }

    private void setRestaurantLogo(String restaurantName, ImageView logoImageView) {
        // Map restaurant names to their corresponding logo resource IDs
        int logoResourceId = R.drawable.black;
        switch (restaurantName) {
            case "McDonald's":
                logoResourceId = R.drawable.mcdonalds;
                break;
            case "Chick-fil-A":
                logoResourceId = R.drawable.chickfila;
                break;
            case "Popeyes Louisiana Kitchen":
                logoResourceId = R.drawable.popeyes;
                break;
            case "Zaxby's":
                logoResourceId = R.drawable.zaxbys;
                break;
            case "Hardee's":
                logoResourceId = R.drawable.hardees;
                break;
            case "Five Guys":
                logoResourceId = R.drawable.fiveguys;
                break;
            case "Taco Bell":
                logoResourceId = R.drawable.tacobell;
                break;
            case "Wendy's":
                logoResourceId = R.drawable.wendys;
                break;
            case "Burger King":
                logoResourceId = R.drawable.burgerking;
                break;
        }

        // Set the restaurant logo
        logoImageView.setImageResource(logoResourceId);
    }
}