package com.example.boinobolsov02.Activities.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.boinobolsov02.HelperClasses.CheckInternet;
import com.example.boinobolsov02.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

    ImageView goBackBtn;
    TextInputLayout phoneNo;
    PinView pinFromUser;
    LinearLayout verifyOTPScreen, forgotPasswordScreen;
    CountryCodePicker countryCodePicker;
    Button nextBtn, verifyCodeBtn;
    FirebaseAuth mAuth;
    String codeBySystem, completePhoneNumber;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Hooks
        goBackBtn = findViewById(R.id.forgot_password_go_back_btn);
        verifyOTPScreen = findViewById(R.id.verify_otp);
        forgotPasswordScreen = findViewById(R.id.forgot_password);
        phoneNo = findViewById(R.id.forget_password_phone_number);
        countryCodePicker = findViewById(R.id.forget_password_country_code_picker);
        nextBtn = findViewById(R.id.forget_password_next_button);
        progressBar = findViewById(R.id.forget_password_progress_bar);
        mAuth = FirebaseAuth.getInstance();
        pinFromUser = findViewById(R.id.verify_otp_pin_view);
        verifyCodeBtn = findViewById(R.id.verify_otp_verify_code_btn);

        //Methods
        verifyPhoneNumber();
        callNextScreenFromOTP();
        goBack();
    }

    private void verifyPhoneNumber() {
        nextBtn.setOnClickListener(view -> {
            //Check connection
            CheckInternet checkInternet = new CheckInternet();
            if (!checkInternet.isConnected(ForgotPassword.this)) {
                checkInternet.showCustomDialog(ForgotPassword.this);
            }

            //Validate Phone Number
            if (!validateFields()) {
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            //Get data from fields
            String _phoneNumber = phoneNo.getEditText().getText().toString().trim();
            if (_phoneNumber.charAt(0) == '0') {
                _phoneNumber = _phoneNumber.substring(1);
            }
            completePhoneNumber = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNumber;

            //Check if user exists in database
            Query checkUser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phoneNo").equalTo(completePhoneNumber);

            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //If user in database call OTP and verify if its the users phone
                    if (snapshot.exists()) {
                        phoneNo.setError(null);
                        phoneNo.setErrorEnabled(false);

                        forgotPasswordScreen.setVisibility(View.GONE);
                        verifyOTPScreen.setVisibility(View.VISIBLE);

                        sendVerificationCodeToUser(completePhoneNumber);

//                        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
//                        intent.putExtra("phoneNo", _completePhoneNumber);
//                        startActivity(intent);
//                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        phoneNo.setError("Numero de telefone não registrado");
                        phoneNo.requestFocus();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ForgotPassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(ForgotPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);

        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
                            intent.putExtra("phoneNo", completePhoneNumber);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(ForgotPassword.this, "Verification not completed, Try Again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    void callNextScreenFromOTP() {
        verifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = Objects.requireNonNull(pinFromUser.getText()).toString();
                if (!code.isEmpty()) {
                    verifyCode(code);
                }

            }
        });
    }

    void goBack() {
        goBackBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);

            Pair[] pairs = new Pair[1];

            pairs[0] = new Pair<View, String>(findViewById(R.id.forgot_password_go_back_btn), "transition_login");

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgotPassword.this, pairs);

            startActivity(intent, options.toBundle());
        });
    }

    private boolean validateFields() {
        String _phoneNumber = Objects.requireNonNull(phoneNo.getEditText()).getText().toString().trim();

        if (_phoneNumber.isEmpty()) {
            phoneNo.setError("Phone number cannot be empty");
            phoneNo.requestFocus();
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }
    }

}
