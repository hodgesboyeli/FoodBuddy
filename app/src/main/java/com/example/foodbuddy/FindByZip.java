package com.example.foodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FindByZip extends AppCompatActivity {

    int zipcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_zip);

        final EditText inputZipcode = (EditText) findViewById(R.id.etZip);
        final Button findByZip = (Button) findViewById(R.id.btFindByZip);
        Button btHome = findViewById(R.id.btHome);
        Button btFavorite = findViewById(R.id.btFavorite);
        Button btUser = findViewById(R.id.btUser);
        findByZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipcode = inputZipcode.getText().toString().trim();
                if (!zipcode.isEmpty()) {
                    // Create an Intent to pass the zipcode to the ResultsActivity
                    Intent intent = new Intent(FindByZip.this, Results.class);
                    intent.putExtra("ZIPCODE", zipcode);
                    startActivity(intent);
                }
            }
        });

        btHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindByZip.this, FindFood.class));
            }
        });


    }
}