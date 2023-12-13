package com.example.foodbuddy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.sparkle);

        final EditText username = findViewById(R.id.tvUsername);
        final EditText password = findViewById(R.id.tvPassword);
        Button btLogin = findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = username.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredUsername.equals("john1.doe") && enteredPassword.equals("password")) {
                    // Start playing sparkle.mp3 only when the credentials are correct
                    mediaPlayer.start();
                    startActivity(new Intent(Login.this, FindFood.class));
                } else {
                    Toast.makeText(getBaseContext(), "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resources when the activity is destroyed
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
