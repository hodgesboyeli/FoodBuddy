package com.example.foodbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddRating extends AppCompatActivity {
    private FirebaseFirestore db;
    private String docID;
    private Restaurants restaurantObject;
    private Float inputRating = 1.0F;
    private String restAddy;
    private String restName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rating);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RESTAURANT OBJECT")) {
            restaurantObject = (Restaurants) intent.getSerializableExtra("RESTAURANT OBJECT");
            restAddy = restaurantObject.getAddress();
            restName = restaurantObject.getName();
        }
        db = FirebaseFirestore.getInstance();
        CollectionReference restaurants = db.collection("Restaurants");
        Query query = restaurants
                .whereEqualTo("name", restaurantObject.getName())
                .whereEqualTo("address", restaurantObject.getAddress());
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Get the document ID
                            docID = documentSnapshot.getId();
                            // Now you can use documentId as needed
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Log.e("Firestore", "Error retrieving documents", e);
                    }
                });

        TextView tvRestName = (TextView) findViewById(R.id.tvRestaurantName);
        TextView tvRestAddy = (TextView) findViewById(R.id.tvRestaurantAddress);
        TextView tvDebug = (TextView) findViewById(R.id.tvDebug);
        EditText etInputRating = (EditText) findViewById(R.id.tvInputRating);
        Button btSubmitRating = (Button) findViewById(R.id.btSubmitRating);
        btSubmitRating.setEnabled(true);

        tvRestName.setText(restName);
        tvRestAddy.setText(restAddy);
        tvDebug.setText("Test: " + restaurantObject.getRating());
        btSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputRatingText = etInputRating.getText().toString();
                inputRating = Float.parseFloat(inputRatingText);
                restaurantObject.incrementRatingCount();
                restaurantObject.updateRatingSum(inputRating);
                Float temp = restaurantObject.getRating();
                restaurantObject.calculateRating();
//                tvDebug.setText("New Test: " + restaurantObject.getRating());
                tvDebug.setText("Restaurant stats: " +
                        "\nPrevious Rating: " + temp +
                        "\nRestaurant Rating Sum: " + restaurantObject.getRatingSum() +
                        "\nRestaurant Rating Count: " + restaurantObject.getRatingCount() +
                        "\n New Restaurant Rating: " + restaurantObject.getRating());
                Map<String, Object> updates = new HashMap<>();
                updates.put("ratingSum", restaurantObject.getRatingSum());
                updates.put("ratingCount", restaurantObject.getRatingCount());
                updates.put("rating", restaurantObject.getRating());
                restaurants.document(docID)
                        .update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "IT WORKED!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle errors
                                Log.e("Firestore", "Error updating document", e);
                            }
                        });
                Toast.makeText(getBaseContext(), "Your rating has been submitted!\nReturn to the previous page.", Toast.LENGTH_SHORT).show();
                btSubmitRating.setEnabled(false);
            }
        });


    }
}