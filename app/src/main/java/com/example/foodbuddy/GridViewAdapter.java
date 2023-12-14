package com.example.foodbuddy;// GridViewAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodbuddy.R;
import com.example.foodbuddy.Restaurants;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Restaurants> {

    public GridViewAdapter(@NonNull Context context, @NonNull List<Restaurants> restaurantsList) {
        super(context, 0, restaurantsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_layout, parent, false);
        }

        Restaurants restaurant = getItem(position);

        if (restaurant != null) {
            ImageView imageView = itemView.findViewById(R.id.imageView);
            TextView ratingTextView = itemView.findViewById(R.id.ratingTextView);
            TextView addressTextView = itemView.findViewById(R.id.addressTextView);

            // Set restaurant image, rating, and address in the grid item
            imageView.setImageResource(getRestaurantImage(restaurant.getName()));
            ratingTextView.setText(String.valueOf(restaurant.getRating()));
            addressTextView.setText(restaurant.getAddress());
        }

        return itemView;
    }

    private int getRestaurantImage(String restaurantName) {
        int logoResourceId = R.drawable.black; // Default image resource

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
            // Add cases for other restaurants as needed
            default:
                // Use the default image if no match is found
                break;
        }

        return logoResourceId;
    }
}
