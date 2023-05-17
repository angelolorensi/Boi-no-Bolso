package com.example.boinobolsov02.Activities.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boinobolsov02.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class Signup2ndClass extends AppCompatActivity {

    ImageView goBackBtn, logoImage;
    TextView titleTxt;
    Button nextBtn;
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2nd_class);

        //Hooks
        logoImage = findViewById(R.id.signup_2nd_class_logo_image);
        nextBtn = findViewById(R.id.signup_2nd_class_next_btn);
        goBackBtn = findViewById(R.id.signup_2nd_class_back_btn);
        titleTxt = findViewById(R.id.signup_2nd_class_title_text);
        countryCodePicker = findViewById(R.id.signup_2nd_class_country_code_picker);
        phoneNumber = findViewById(R.id.signup_2nd_class_phone_number);

        //methods
        goBack();
        callNextSignupScreen();
    }

    void goBack(){

        goBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Signup1stClass.class);

            Pair[] pairs = new Pair[1];

            pairs[0] = new Pair<View, String>(findViewById(R.id.signup_2nd_class_back_btn), "transition_signup_first_class");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup2ndClass.this, pairs);

            startActivity(intent, options.toBundle());
        });
    }

    private void callNextSignupScreen() {
        nextBtn.setOnClickListener(view -> {
            //Get data from previous activity
            String _fullname = getIntent().getStringExtra("fullname");
            String _username = getIntent().getStringExtra("username");
            String _email = getIntent().getStringExtra("email");
            String _password = getIntent().getStringExtra("password");
            String _date = getIntent().getStringExtra("date");
            String _gender = getIntent().getStringExtra("gender");

            //Get Data form field an unite them
            String getUserEnteredPhoneNumber = Objects.requireNonNull(phoneNumber.getEditText()).getText().toString();
            String _phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + getUserEnteredPhoneNumber;

            Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

            intent.putExtra("fullname", _fullname);
            intent.putExtra("username", _username);
            intent.putExtra("email", _email);
            intent.putExtra("password", _password);
            intent.putExtra("date", _date);
            intent.putExtra("gender", _gender);
            intent.putExtra("phoneNo", _phoneNo);

            //Add Transitions
            Pair[] pairs = new Pair[4];

            pairs[0] = new Pair<View,String>(goBackBtn, "transition_back_arrow_btn");
            pairs[1] = new Pair<View,String>(nextBtn, "transition_next_btn");
            pairs[2] = new Pair<View,String>(titleTxt, "transition_title_text");
            pairs[3] = new Pair<View,String>(logoImage, "transition_logo");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup2ndClass.this, pairs);

            startActivity(intent, options.toBundle());
        });


    }
}