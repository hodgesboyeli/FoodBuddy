package com.example.foodbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
            TextView ratingTextView = itemView.findViewById(R.id.ratingTextView);

            // Set restaurant name
            nameTextView.setText(restaurant.getName());

            // Set restaurant address
            addressTextView.setText(restaurant.getAddress() + ", " +  restaurant.getZipCode());

            // Set restaurant rating
            ratingTextView.setText(String.valueOf(restaurant.getRating()));
        }

        return itemView;
    }
}
