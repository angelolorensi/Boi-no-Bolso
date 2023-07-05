package com.example.boinobolsov02.Activities.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.boinobolsov02.Activities.Home;
import com.example.boinobolsov02.Activities.LoginSignup.ForgotPassword;
import com.example.boinobolsov02.Activities.NotLoggedIn;
import com.example.boinobolsov02.HelperClasses.SessionManager;
import com.example.boinobolsov02.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    ImageView goBackBtn, profileImage;
    TextView username, email, phone, changePassword;
    RelativeLayout purchasesBtn, salesBtn, listingsBtn;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        //Hooks
        goBackBtn = findViewById(R.id.profile_back_btn);
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        phone = findViewById(R.id.profile_info_phoneNo_info);
        purchasesBtn = findViewById(R.id.profile_purchases);
        salesBtn = findViewById(R.id.profile_sales);
        listingsBtn = findViewById(R.id.profile_listings);
        changePassword = findViewById(R.id.profile_info_password_info);

        //Methods
        loadData();
        setButtons();
        checkLoggedIn();
    }

    private void checkLoggedIn(){
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        boolean isLogged = sessionManager.checkLogin();
        if (!isLogged){
            Intent intent = new Intent(getApplicationContext(), NotLoggedIn.class);
            intent.putExtra("message", "faça login para ter acesso a pagina de perfil do usuario");
            startActivity(intent);
            finish();
        }
    }

    void loadData(){
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        String phone_ = sessionManager.getUserDetailsFromSession().get(SessionManager.KEY_PHONENUMBER);

        database = FirebaseDatabase.getInstance().getReference("Users");
        Query query = database.orderByChild("phoneNo").equalTo(phone_);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String fullname_ = dataSnapshot.child("fullname").getValue(String.class);
                    String email_ = dataSnapshot.child("email").getValue(String.class);
                    String imageUrl_ = dataSnapshot.child("imageUrl").getValue(String.class);

                    email.setText(email_);
                    username.setText(fullname_);
                    phone.setText(phone_);
                    Glide.with(UserProfile.this).load(imageUrl_).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setButtons(){
        goBackBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Home.class)));

        listingsBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MyListings.class)));

        salesBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MySales.class)));

        changePassword.setOnClickListener(view -> changePasswordBtn());
    }

    void changePasswordBtn(){
        Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
        intent.putExtra("changePasswordFromProfile", "changePassword");
        startActivity(intent);
    }
}