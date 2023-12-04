package com.example.foodbuddy;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

//https://www.google.com/maps/dir//(STRING)?entry=ttu
public class Results extends AppCompatActivity {

    private EditText tvSearch;
    private ListView lvResults;
    private Button btSearch;
    private FirebaseFirestore db;
    private String zipCode;

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

        // Set click listener for search button
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResults(intent);;
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
        String restaurantName = tvSearch.getText().toString();
        if (restaurantName.isEmpty()){
            db.collection("Restaurants")
                    .whereEqualTo("zipCode", Integer.parseInt(zipCode))
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Restaurants> restaurants = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Restaurants restaurant = document.toObject(Restaurants.class);
                                    restaurants.add(restaurant);
                                    restaurant.clearRatingValues();
                                    Log.d(TAG, "Zipcode is: " + document.getDouble("zipCode"));

                                }

                                // Update the ListView with the retrieved restaurants
                                RestaurantListAdapter adapter = new RestaurantListAdapter(Results.this, restaurants);
                                lvResults.setAdapter(adapter);
                                lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
        }else{
            db.collection("Restaurants")
                    .whereEqualTo("zipCode", Integer.parseInt(zipCode))
                    .whereEqualTo("name", restaurantName)
                    .orderBy("rating", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Restaurants> restaurants = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Restaurants restaurant = document.toObject(Restaurants.class);
                                    restaurants.add(restaurant);
                                }

                                // Update the ListView with the retrieved restaurants
                                RestaurantListAdapter adapter = new RestaurantListAdapter(Results.this, restaurants);
                                lvResults.setAdapter(adapter);
                                lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
    }

    private void showChoicesDialog(Restaurants clickedItem) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_diolog_choices);

        Button btGetDir = dialog.findViewById(R.id.btGetDirections);
        Button btAddRate = dialog.findViewById(R.id.btAddRating);

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

        dialog.show();
    }
}
