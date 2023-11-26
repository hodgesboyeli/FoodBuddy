package com.example.foodbuddy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class Results extends AppCompatActivity {

    private EditText tvSearch;
    private ListView lvResults;
    private Button btSearch;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Initialize views
        tvSearch = findViewById(R.id.tvSearch);
        lvResults = findViewById(R.id.lvResults);
        btSearch = findViewById(R.id.btSearch);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        // Set click listener for search button
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
    }

    private void performSearch() {
        String zipCode = tvSearch.getText().toString();

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
}
