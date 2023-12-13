package com.example.foodbuddy;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {

    private EditText tvSearch;
    private ListView lvResults;
    private Button btSearch;
    private FirebaseFirestore db;
    private String zipCode;

    private String docID;

    private List<Restaurants> allRestaurants = new ArrayList<>();
    private RestaurantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Button btHome = findViewById(R.id.btHome);
        Button btFavorite = findViewById(R.id.btFavorite);
        Button btUser = findViewById(R.id.btUser);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ZIPCODE")) {
            zipCode = intent.getStringExtra("ZIPCODE");
        }

        // Initialize views
        tvSearch = findViewById(R.id.tvSearch);
        lvResults = findViewById(R.id.lvFavorites);
        btSearch = findViewById(R.id.btSearch);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        showResults(intent);

        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Update the results as the user types
                showResults(intent);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this implementation
            }
        });

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Results.this, FindFood.class));
            }
        });

        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Results.this, Favorites.class));
            }
        });
    }

    private void showResults(Intent intent) {
        String restaurantName = tvSearch.getText().toString().toLowerCase().replaceAll("[\'-]", "");

        // Fetch all restaurants if the query is empty
        if (restaurantName.isEmpty()) {
            db.collection("Restaurants")
                    .whereEqualTo("zipCode", Integer.parseInt(zipCode))
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                allRestaurants.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Restaurants restaurant = document.toObject(Restaurants.class);
                                    restaurant.setId(document.getId());
                                    allRestaurants.add(restaurant);
                                    restaurant.clearRatingValues();
                                }
                                updateListView(allRestaurants);
                            } else {
                                // Handle errors
                                Exception exception = task.getException();
                                if (exception != null) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            // Filter locally based on the user's input
            List<Restaurants> filteredRestaurants = new ArrayList<>();
            for (Restaurants restaurant : allRestaurants) {
                String name = restaurant.getName().toLowerCase().replaceAll("[\'-]", "");
                if (name.contains(restaurantName)) {
                    filteredRestaurants.add(restaurant);
                }
            }
            updateListView(filteredRestaurants);
        }
    }

    private void updateListView(List<Restaurants> restaurants) {
        adapter = new RestaurantListAdapter(Results.this, restaurants);
        lvResults.setAdapter(adapter);

        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurants clickedItem = (Restaurants) parent.getItemAtPosition(position);
                showChoicesDialog(clickedItem);
            }
        });
    }

    private void showChoicesDialog(Restaurants clickedItem) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_diolog_choices);

        Button btGetDir = dialog.findViewById(R.id.btGetDirections);
        Button btAddRate = dialog.findViewById(R.id.btAddRating);
        Button btMakeFavorite = dialog.findViewById(R.id.btUnfavorite);

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
                Intent intent = new Intent(Results.this, AddRating.class);
                intent.putExtra("RESTAURANT OBJECT", clickedItem);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btMakeFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get a reference to the specific document in the "Restaurants" collection
                String restaurantId = clickedItem.getId(); // Assuming you have a method to get the restaurant ID
                if (restaurantId != null) {
                    db.collection("Restaurants").document(restaurantId)
                            .update("isFavorite", true)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Update the UI or show a toast message
                                        Toast.makeText(Results.this, "Restaurant Favorited!", Toast.LENGTH_SHORT).show();
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
