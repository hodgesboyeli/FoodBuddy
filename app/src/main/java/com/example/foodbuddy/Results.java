package com.example.foodbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
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
                performSearch();
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
        // Query the Firestore database
        db.collection("Restaurants")
                .whereEqualTo("zipCode", Integer.parseInt(zipCode))
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
    private void performSearch() {
        String restaurantName = tvSearch.getText().toString();
        // Query the Firestore database
        db.collection("Restaurants")
                .whereEqualTo("zipCode", Integer.parseInt(zipCode))
                .whereEqualTo("name", restaurantName)
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
