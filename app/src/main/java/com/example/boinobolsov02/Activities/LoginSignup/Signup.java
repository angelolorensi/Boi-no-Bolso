package com.example.boinobolsov02.Activities.LoginSignup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.boinobolsov02.HelperClasses.Models.User;
import com.example.boinobolsov02.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    LinearLayout firstScreen, secondScreen, thirdScreen;
    ImageView profileImage, goBackBtnPart1, goBackBtnPart2, goBackBtnPart3;
    Uri imageUri;
    TextInputLayout fullname, username, email, password, phoneNo;
    RadioGroup genderPicker;
    RadioButton selectedGender;
    DatePicker agePicker;
    CountryCodePicker countryCodePicker;
    Button part1Btn, part2Btn, part3Btn;
    PinView pinFromUser;
    String codeBySystem, whatToDO;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //hooks
        hookElements();

        choosePicture();
        changeScreen();
        goBackBtn();
    }

    private void goBackBtn() {
        goBackBtnPart1.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
        goBackBtnPart2.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
        goBackBtnPart3.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }

    private void choosePicture() {
        profileImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
        }
    }

    void saveNewUser() {
        //Get data from fields
        String _fullname = Objects.requireNonNull(fullname.getEditText()).getText().toString().trim();
        String _username = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
        String _email = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
        String _password = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
        //Get gender pick
        selectedGender = findViewById(genderPicker.getCheckedRadioButtonId());
        String _gender = selectedGender.getText().toString();
        //Get datePicker and concatenate dates in one string
        int day = agePicker.getDayOfMonth();
        int month = agePicker.getMonth();
        int year = agePicker.getYear();
        String _date = day + "/" + month + "/" + year;
        //Get user phone number
        String getUserEnteredPhoneNumber = Objects.requireNonNull(phoneNo.getEditText()).getText().toString();
        String _phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + getUserEnteredPhoneNumber;

        User newUser = new User(_fullname, _username, _email, _phoneNo, _password, _date, _gender, " ");

        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/UserProfileImages/" + randomKey);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                newUser.setImageUrl(uri.toString());
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(_phoneNo).setValue(newUser);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Signup.this, "Falha ao enviar imagem", Toast.LENGTH_SHORT).show();
                    }
                });

        Toast.makeText(this, "Usuario criado com sucesso!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Login.class));

    }

    void changeScreen() {
        part1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validate fields
                if (!validateFullName() | !validateUsername() | !validateEmail() | !validatePassword() | !validateAge() | !validateGender()) {
                    return;
                }
                firstScreen.setVisibility(View.GONE);
                secondScreen.setVisibility(View.VISIBLE);
            }
        });

        part2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUserEnteredPhoneNumber = Objects.requireNonNull(phoneNo.getEditText()).getText().toString();
                String _phoneNo = "+" + countryCodePicker.getSelectedCountryCode() + getUserEnteredPhoneNumber;
                sendVerificationCodeToUser(_phoneNo);
                secondScreen.setVisibility(View.GONE);
                thirdScreen.setVisibility(View.VISIBLE);
            }
        });
        part3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = pinFromUser.getText().toString();
                if (!code.isEmpty()) {
                    verifyCode(code);
                }
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
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
                    Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            saveNewUser();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Signup.this, "Verificação não completa, Tente novamente!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = agePicker.getYear();
        int isAgeValid = currentYear - userAge;

        if (isAgeValid < 14) {
            Toast.makeText(this, "Você não possui a idade mínima", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateGender() {
        if (genderPicker.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecione um gênero!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFullName() {
        String val = Objects.requireNonNull(fullname.getEditText()).getText().toString().trim();

        if (val.isEmpty()) {
            fullname.setError("Campo não deve estar vazio!");
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
            username.setError("Campo não deve estar vazio!");
            return false;
        } else if (val.length() > 20) {
            username.setError("Nome de usuario muito grande!");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("Não é permitido espaços no nome de usuario!");
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
            email.setError("Campo não deve estar vazio!");
            return false;
        } else if (!val.matches(checkEmail)) {
            email.setError("Email inválido!");
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
            password.setError("Campo não deve estar vazio!");
            password.requestFocus();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(val).matches()) {
            password.setError("Senha deve conter no minimo 8 letras, 1 maiúscula, 1 minúscula e um caractere especial!");
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

    private void hookElements() {
        firstScreen = findViewById(R.id.signup_part1);
        secondScreen = findViewById(R.id.signup_part2);
        thirdScreen = findViewById(R.id.signup_part3);
        profileImage = findViewById(R.id.signup_part1_image_select);
        fullname = findViewById(R.id.signup_part1_fullname);
        username = findViewById(R.id.signup_part1_username);
        email = findViewById(R.id.signup_part1_email);
        password = findViewById(R.id.signup_part1_password);
        genderPicker = findViewById(R.id.signup_part1_radio_group);
        agePicker = findViewById(R.id.signup_part1_age_picker);
        goBackBtnPart1 = findViewById(R.id.signup_back_btn1);
        goBackBtnPart2 = findViewById(R.id.signup_back_btn2);
        goBackBtnPart3 = findViewById(R.id.signup_back_btn3);
        countryCodePicker = findViewById(R.id.signup_part2_country_code_picker);
        phoneNo = findViewById(R.id.signup_part2_phone_number);
        part1Btn = findViewById(R.id.signup_part1_next_btn);
        part2Btn = findViewById(R.id.signup_part2_next_btn);
        part3Btn = findViewById(R.id.signup_part3_verify_code_btn);
        pinFromUser = findViewById(R.id.signup_part3_pin_view);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
    }
}