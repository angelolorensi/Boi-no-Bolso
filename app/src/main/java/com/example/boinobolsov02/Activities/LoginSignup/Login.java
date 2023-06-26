package com.example.boinobolsov02.Activities.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.boinobolsov02.Activities.Home;
import com.example.boinobolsov02.HelperClasses.SessionManager;
import com.example.boinobolsov02.HelperClasses.CheckInternet;
import com.example.boinobolsov02.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity {

    ProgressBar progressBar;
    Button forgotPasswordBtn, signInBtn, loginBtn;
    CountryCodePicker countryCodePicker;
    TextInputLayout phoneNo, password;
    TextInputEditText phoneNoEditTxt, passwordEditTxt;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Hooks
        progressBar = findViewById(R.id.progressBar);
        forgotPasswordBtn = findViewById(R.id.login_forget_password_btn);
        signInBtn = findViewById(R.id.login_signin_btn);
        loginBtn = findViewById(R.id.login_login_btn);
        countryCodePicker = findViewById(R.id.login_country_code_picker);
        phoneNo = findViewById(R.id.login_phone_number);
        password = findViewById(R.id.login_password);
        rememberMe = findViewById(R.id.login_remember_me);
        passwordEditTxt = findViewById(R.id.login_password_edit_txt);
        phoneNoEditTxt = findViewById(R.id.login_phone_number_edit_txt);

        //Methods
        goToForgotPasswordScreen();
        goToSignUpScreen();
        login();

        //Check if phoneNo and password is saved in Shared Preferences
        SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
        if (sessionManager.checkRememberMe()){
            HashMap<String,String> rememberMeDetails = sessionManager.getRememberMeDetailsFromSession();
            phoneNoEditTxt.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPHONENUMBER));
            passwordEditTxt.setText(rememberMeDetails.get(SessionManager.KEY_SESSIONPASSWORD));
        }
    }

    void login(){
        loginBtn.setOnClickListener(view -> {
            //Check if internet is online
            CheckInternet checkInternet = new CheckInternet();
            if (!checkInternet.isConnected(Login.this)) {
                checkInternet.showCustomDialog(Login.this);
            }
            //Validate fields
            if (!validateFields()) {
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            //Get values
            String _phoneNumber = Objects.requireNonNull(phoneNo.getEditText()).getText().toString().trim();
            String _password = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
            //Remove zeros at the start
            if (_phoneNumber.charAt(0) == '0') {
                _phoneNumber = _phoneNumber.substring(1);
            }
            //Join selected countrycodepicker with phone number
            String _completePhoneNumber = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNumber;
            //Check if user exists in database
            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(_completePhoneNumber);
            //Check if remember-me checkbox is checked
            if (rememberMe.isChecked()){
                SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_REMEMBERME);
                sessionManager.createRememberMeSession(_phoneNumber, _password);
            }
            //Authenticate
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        phoneNo.setError(null);
                        phoneNo.setErrorEnabled(false);

                        String sysPassword = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                        //if password exists and match with users password then get other fields from database
                        assert sysPassword != null;
                        if (sysPassword.equals(_password)) {
                            password.setError(null);
                            password.setErrorEnabled(false);

                            //Get users data from firebase database
                            String _fullname = snapshot.child(_completePhoneNumber).child("fullname").getValue(String.class);
                            String _username = snapshot.child(_completePhoneNumber).child("username").getValue(String.class);
                            String _email = snapshot.child(_completePhoneNumber).child("email").getValue(String.class);
                            String _phoneNo = snapshot.child(_completePhoneNumber).child("phoneNo").getValue(String.class);
                            String _password = snapshot.child(_completePhoneNumber).child("password").getValue(String.class);
                            String _dateOfBirth = snapshot.child(_completePhoneNumber).child("date").getValue(String.class);
                            String _gender = snapshot.child(_completePhoneNumber).child("gender").getValue(String.class);

                            //Create Session
                            SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_USERSESSION);
                            sessionManager.createLoginSession(_fullname, _username, _email, _phoneNo, _password, _dateOfBirth, _gender);

                            startActivity(new Intent(getApplicationContext(), Home.class));

                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Login.this, "Usuario não existe!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    void goToForgotPasswordScreen() {
        forgotPasswordBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);

            Pair[] pairs = new Pair[1];

            pairs[0] = new Pair<View, String>(findViewById(R.id.login_forget_password_btn), "transition_forgot_password");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);

            startActivity(intent, options.toBundle());
        });
    }

    void goToSignUpScreen() {
        signInBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Signup1stClass.class);

            Pair[] pairs = new Pair[1];

            pairs[0] = new Pair<View, String>(findViewById(R.id.login_signin_btn), "transition_signup");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);

            startActivity(intent, options.toBundle());
        });
    }

    private boolean validateFields() {

        String _phoneNumber = Objects.requireNonNull(phoneNo.getEditText()).getText().toString().trim();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

        if (_phoneNumber.isEmpty()) {
            phoneNo.setError("Campo numero de telefone é obrigatório");
            phoneNo.requestFocus();
            return false;
        } else if (_password.isEmpty()) {
            password.setError("Campo senha é obrigatório");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }

    }

}