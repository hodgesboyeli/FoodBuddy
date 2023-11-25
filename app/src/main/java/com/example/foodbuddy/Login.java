package com.example.foodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText username = findViewById(R.id.tvUsername);
        final EditText password = findViewById(R.id.tvPassword);
        Button btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredUsername.equals("john1.doe") && enteredPassword.equals("password")) {
                    startActivity(new Intent(Login.this, FindFood.class));
                } else {
                    Toast.makeText(getBaseContext(), "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
