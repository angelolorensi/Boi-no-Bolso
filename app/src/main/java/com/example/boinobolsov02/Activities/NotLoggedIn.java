package com.example.boinobolsov02.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.boinobolsov02.HelperClasses.SessionManager;
import com.example.boinobolsov02.R;

public class NotLoggedIn extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_logged_in);

        text = findViewById(R.id.textView);

        String message = getIntent().getStringExtra("message");

        text.setText(message);
    }

}