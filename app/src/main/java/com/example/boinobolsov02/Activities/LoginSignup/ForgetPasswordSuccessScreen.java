package com.example.boinobolsov02.Activities.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.boinobolsov02.R;

public class ForgetPasswordSuccessScreen extends AppCompatActivity {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_sucess_screen);

        loginBtn = findViewById(R.id.forget_password_sucess_message_login_button);

        loginBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }
}