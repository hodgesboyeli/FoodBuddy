package com.example.foodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView lvFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Button btHome = findViewById(R.id.btHome);
        Button btFavorite = findViewById(R.id.btFavorite);
        Button btUser = findViewById(R.id.btLogout);

        // Initialize views
        lvFavorites = findViewById(R.id.lvFavorites);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Set click listeners for buttons
        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Favorites.this, FindFood.class));
            }
        });

        btUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Favorites.this, Login.class));
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing as it's already on the Favorites page
            }
        });

        // Show the list of favorite restaurants
        showFavorites();
    }

    private void showFavorites() {
        db.collection("Restaurants")
                .whereEqualTo("isFavorite", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Restaurants> favoritesList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Restaurants restaurant = document.toObject(Restaurants.class);
                                restaurant.setId(document.getId());
                                favoritesList.add(restaurant);
                                restaurant.clearRatingValues();
                            }

                            // Update the ListView with the retrieved favorite restaurants
                            RestaurantListAdapter adapter = new RestaurantListAdapter(Favorites.this, favoritesList);
                            lvFavorites.setAdapter(adapter);
                            lvFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Handle item click if needed
                                    Restaurants clickedItem = (Restaurants) parent.getItemAtPosition(position);
                                    showChoicesDialog(clickedItem);
                                }
                            });
                        } else {
                            // Handle errors
                            Exception exception = task.getException();
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void showChoicesDialog(Restaurants clickedItem) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_diolog_favorites_choices);

        Button btGetDir = dialog.findViewById(R.id.btGetDirections);
        Button btAddRate = dialog.findViewById(R.id.btAddRating);
        Button btUnfavorite = dialog.findViewById(R.id.btUnfavorite);

        String restAddress = clickedItem.getAddress() + clickedItem.getZipCode();
        String restName = clickedItem.getName();

        String googleMapsURL = "https://www.google.com/maps/dir//" + restAddress + "?entry=ttu";
        btGetDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsURL));
                startActivity(browserIntent);
                dialog.dismiss();
            }
        });

        btAddRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorites.this, AddRating.class);
                intent.putExtra("RESTAURANT OBJECT", clickedItem);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btUnfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restaurantId = clickedItem.getId();
                if (restaurantId != null) {
                    db.collection("Restaurants").document(restaurantId)
                            .update("isFavorite", false)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Update the UI or show a toast message
                                        Toast.makeText(Favorites.this, "Removed Restaurant from Favorites", Toast.LENGTH_SHORT).show();

                                        // Reload the favorites list after successful removal
                                        showFavorites();
                                    } else {
                                        // Handle errors
                                        Exception exception = task.getException();
                                        if (exception != null) {
                                            exception.printStackTrace();
                                        }
                                    }
                                }
                            });
                    dialog.dismiss(); // Close the dialog after updating the favorite status
                }
            }
        });


        dialog.show();
    }
}
