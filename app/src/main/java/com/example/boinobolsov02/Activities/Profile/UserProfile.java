package com.example.boinobolsov02.Activities.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boinobolsov02.Activities.Home;
import com.example.boinobolsov02.Activities.LoginSignup.Login;
import com.example.boinobolsov02.Database.SessionManager;
import com.example.boinobolsov02.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    ImageView goBackBtn, profileImage;
    TextView username, email, phone, adress, cep;
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
        adress = findViewById(R.id.profile_info_adress_info);
        cep = findViewById(R.id.profile_info_cep_info);
        purchasesBtn = findViewById(R.id.profile_purchases);
        salesBtn = findViewById(R.id.profile_sales);
        listingsBtn = findViewById(R.id.profile_listings);

        //Methods
        loadData();
        setGoBackBtn();
    }

    void loadData(){
        SessionManager sessionManager = new SessionManager(UserProfile.this, SessionManager.SESSION_USERSESSION);
        String phone_ = sessionManager.getUserDetailsFromSession().get(sessionManager.KEY_PHONENUMBER);

        database = FirebaseDatabase.getInstance().getReference("Users");
        Query query = database.orderByChild(phone_).equalTo(phone_);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String fullname_ = snapshot.child("fullname").getValue(String.class);
                String email_ = snapshot.child("email").getValue(String.class);

                email.setText(email_);
                username.setText(fullname_);
                phone.setText(phone_);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setGoBackBtn(){
        goBackBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Home.class)));
    }
}