package com.example.boinobolsov02.Activities.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boinobolsov02.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

public class Signup1stClass extends AppCompatActivity {

    ImageView goBackBtn, logoImage;
    TextView titleTxt;
    Button nextBtn;
    TextInputLayout fullname, username, email, password;
    RadioGroup radioGroup;
    RadioButton selectedGender;
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1st_class);

        //Hooks
        logoImage = findViewById(R.id.signup_1nd_class_logo_image);
        goBackBtn = findViewById(R.id.signup_back_btn);
        titleTxt = findViewById(R.id.signup_title_text);
        nextBtn = findViewById(R.id.signup_next_btn);
        fullname = findViewById(R.id.signup_fullname);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        radioGroup = findViewById(R.id.radio_group);
        datePicker = findViewById(R.id.age_picker);

        //Methods
        goBack();
        callNextScreen();
    }

    void callNextScreen(){
        nextBtn.setOnClickListener(view -> {

            //Validate fields
            if(!validateFullName() | !validateUsername() | !validateEmail() | !validatePassword() | !validateAge() | !validateGender()){
                return;
            }

            //Get data from fields
            String _fullname = Objects.requireNonNull(fullname.getEditText()).getText().toString().trim();
            String _username = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
            String _email = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
            String _password = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
            //Get gender pick
            selectedGender = findViewById(radioGroup.getCheckedRadioButtonId());
            String _gender = selectedGender.getText().toString();
            //Get datePicker and concatenate dates in one string
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();
            String _date = day + "/" + month + "/" + year;

            Intent intent = new Intent(getApplicationContext(), Signup2ndClass.class);
            intent.putExtra("fullname", _fullname);
            intent.putExtra("username", _username);
            intent.putExtra("email", _email);
            intent.putExtra("password", _password);
            intent.putExtra("date", _date);
            intent.putExtra("gender", _gender);

            //Add Transitions and call next screen
            Pair[] pairs = new Pair[4];

            pairs[0] = new Pair<View,String>(goBackBtn, "transition_back_arrow_btn");
            pairs[1] = new Pair<View,String>(nextBtn, "transition_next_btn");
            pairs[2] = new Pair<View,String>(titleTxt, "transition_title_text");
            pairs[3] = new Pair<View,String>(logoImage, "transition_logo");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup1stClass.this, pairs);

            startActivity(intent, options.toBundle());

        });
    }

    void goBack(){
        goBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);

            Pair[] pairs = new Pair[1];

            pairs[0] = new Pair<View, String>(findViewById(R.id.signup_back_btn), "transition_login");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup1stClass.this, pairs);

            startActivity(intent, options.toBundle());
        });
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 14) {
            Toast.makeText(this, "You are not eligible to apply", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select gender!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFullName() {
        String val = Objects.requireNonNull(fullname.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            fullname.setError("Field cannot be empty");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateUsername() {
        String val = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty!");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username too big!");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("No white spaces are allowed!");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateEmail() {
        String val = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
        String checkEmail = "[a-zA-Z0-0._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty!");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Invalid email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        String val = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
        Pattern PASSWORD_PATTERN = Pattern.compile(
                "^(?=.*[0-9])" +
                        "(?=.*[a-z])" +
                        "(?=.*[A-Z])" +
                        //"(?=.*[!@#&()–[{}]:;',?/*~$^+=<>])" +
                        ".{6,20}" +
                        "$");

        if (val.isEmpty()) {
            password.setError("Field cannot be empty!");
            password.requestFocus();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(val).matches()) {
            password.setError("Password should contain a minimum of 8 letters, 1 uppercase, 1 lowercase and a special character!");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }
}