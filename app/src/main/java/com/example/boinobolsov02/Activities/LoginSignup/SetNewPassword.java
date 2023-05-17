package com.example.boinobolsov02.Activities.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.boinobolsov02.HelperClasses.CheckInternet;
import com.example.boinobolsov02.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SetNewPassword extends AppCompatActivity {

    TextInputLayout newPassword, confirmPassword;
    Button okButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        okButton = findViewById(R.id.new_password_ok_button);
        progressBar = findViewById(R.id.new_password_progress_bar);

        setNewPassword();
    }

    private void setNewPassword() {
        okButton.setOnClickListener(view -> {
            //Check connection
            CheckInternet checkInternet = new CheckInternet();
            if (!checkInternet.isConnected(SetNewPassword.this)) {
                checkInternet.showCustomDialog(SetNewPassword.this);
            }

            //Validate Phone Number
            if (!validatePassword() | !validateConfirmPassword()) {
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            //Get data from input field
            String _newPassword = Objects.requireNonNull(newPassword.getEditText()).getText().toString().trim();
            String _phoneNumber = getIntent().getStringExtra("phoneNo");

            //Update Password in Firebase Database
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(_phoneNumber).child("password").setValue(_newPassword);

            //Go to next activity
            startActivity(new Intent(getApplicationContext(), ForgetPasswordSuccessScreen.class));
            finish();
        });
    }

    private boolean validatePassword() {
        String _newPassword = Objects.requireNonNull(newPassword.getEditText()).getText().toString().trim();
        String _confirmPassword = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString().trim();

        if (_newPassword.isEmpty()) {
            newPassword.setError("Phone number cannot be empty");
            newPassword.requestFocus();
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String _confirmPassword = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString().trim();

        if (_confirmPassword.isEmpty()) {
            confirmPassword.setError("Phone number cannot be empty");
            confirmPassword.requestFocus();
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }
}