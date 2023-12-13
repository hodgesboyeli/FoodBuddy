package com.example.foodbuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FindBy extends AppCompatActivity {

    private EditText inputText;
    private EditText zipInputText;
    private EditText stateInputText;
    private EditText cityInputText;
    private TextView enterTextView;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by);

        zipInputText = findViewById(R.id.etZip);
        stateInputText = findViewById(R.id.etState);
        cityInputText = findViewById(R.id.etCity);
        enterTextView = findViewById(R.id.tvEnter);
        radioGroup = findViewById(R.id.radioGroup);

        // Set state radio button as checked by default
        RadioButton stateRadioButton = findViewById(R.id.rbState);
        stateRadioButton.setChecked(true);

        Button btFindByState = findViewById(R.id.btFindByState);
        Button btFindByCity = findViewById(R.id.btFindByCity);
        Button btFindByZip = findViewById(R.id.btFindByZip);


        // Set radio group change listener to update the text view and show/hide EditText accordingly
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbZipcode) {
                    enterTextView.setText("Enter Zipcode:");
                    zipInputText.setVisibility(View.VISIBLE);
                    btFindByZip.setVisibility(View.VISIBLE);
                    stateInputText.setVisibility(View.GONE);
                    btFindByState.setVisibility(View.GONE); // Hide state button
                    cityInputText.setVisibility(View.GONE);
                    btFindByCity.setVisibility(View.GONE); // Hide city button
                } else if (checkedId == R.id.rbCity) {
                    enterTextView.setText("Enter City:");
                    zipInputText.setVisibility(View.GONE);
                    btFindByZip.setVisibility(View.GONE);
                    stateInputText.setVisibility(View.GONE);
                    btFindByState.setVisibility(View.GONE);
                    cityInputText.setVisibility(View.VISIBLE);
                    btFindByCity.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rbState) {
                    enterTextView.setText("Enter State:");
                    zipInputText.setVisibility(View.GONE);
                    btFindByZip.setVisibility(View.GONE);
                    stateInputText.setVisibility(View.VISIBLE);
                    btFindByState.setVisibility(View.VISIBLE);
                    cityInputText.setVisibility(View.GONE);
                    btFindByCity.setVisibility(View.GONE);
                }
            }
        });
        btFindByState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = stateInputText.getText().toString().trim();
                if (!state.isEmpty()) {
                    // Create an Intent to pass the state to the ResultsActivity
                    Intent intent = new Intent(FindBy.this, Results.class);
                    intent.putExtra("STATE", state);
                    startActivity(intent);
                }
            }
        });
        btFindByCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityInputText.getText().toString().trim();
                if (!city.isEmpty()) {
                    // Create an Intent to pass the city to the ResultsActivity
                    Intent intent = new Intent(FindBy.this, Results.class);
                    intent.putExtra("CITY", city);
                    startActivity(intent);
                }
            }
        });


        btFindByZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipcode = zipInputText.getText().toString().trim();
                Log.d("FindByActivity", "Zipcode: " + zipcode);
                if (!zipcode.isEmpty()) {
                    // Create an Intent to pass the zipcode to the ResultsActivity
                    Intent intent = new Intent(FindBy.this, Results.class);
                    intent.putExtra("ZIPCODE", zipcode);
                    startActivity(intent);
                }
            }
        });
    }
}
