package com.example.boinobolsov02.Activities.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.boinobolsov02.Activities.ProductPage;
import com.example.boinobolsov02.HelperClasses.Adapters.MylistingsAdapter;

import com.example.boinobolsov02.HelperClasses.Models.Listing;
import com.example.boinobolsov02.HelperClasses.RecyclerViewInterface;
import com.example.boinobolsov02.HelperClasses.SessionManager;
import com.example.boinobolsov02.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyListings extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    DatabaseReference database;
    ImageView goBackBtn;
    ArrayList<Listing> listings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        recyclerView = findViewById(R.id.mylistings_recycler);
        goBackBtn = findViewById(R.id.mylistings_back_btn);

        recyclerView();
        setGoBackBtn();
    }

    void recyclerView(){
        //recycler settings
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //fetching data from database
        database = FirebaseDatabase.getInstance().getReference("Listings");
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        String userId = sessionManager.getUserDetailsFromSession().get(SessionManager.KEY_PHONENUMBER);
        Query query = database.orderByChild("ownerId").equalTo(userId);

        //settings the adapter to the recycler
        adapter = new MylistingsAdapter(listings, this);
        recyclerView.setAdapter(adapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String _imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String _title = dataSnapshot.child("title").getValue(String.class);
                    String _breed = dataSnapshot.child("breed").getValue(String.class);
                    String _maturity = dataSnapshot.child("maturity").getValue(String.class);
                    String _quantity = dataSnapshot.child("quantity").getValue(String.class);
                    String _description = dataSnapshot.child("description").getValue(String.class);
                    Double _price = dataSnapshot.child("price").getValue(Double.class);
                    String _livestockCategory = dataSnapshot.child("livestockCategory").getValue(String.class);
                    Boolean _allowSeparatedSale = dataSnapshot.child("allowSeparatedSell").getValue(Boolean.class);
                    String _city = dataSnapshot.child("city").getValue(String.class);
                    String _state = dataSnapshot.child("state").getValue(String.class);

                    Listing mylistingsHelper = new Listing(_title, _livestockCategory, _maturity, _breed, _price, _quantity, _description, _city, _state, _allowSeparatedSale, _imageUrl ,false);

                    listings.add(mylistingsHelper);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void setGoBackBtn() {
        goBackBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), UserProfile.class)));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), ProductPage.class);

        intent.putExtra("breed", listings.get(position).getBreed());
        intent.putExtra("quantity", listings.get(position).getQuantity());
        intent.putExtra("price", listings.get(position).getPrice());
        intent.putExtra("title", listings.get(position).getTitle());
        intent.putExtra("maturity", listings.get(position).getMaturity());
        intent.putExtra("imageUrl", listings.get(position).getImageUrl());
        intent.putExtra("description", listings.get(position).getDescription());
        intent.putExtra("allowSeparatedSale", listings.get(position).getAllowSeparatedSell());
        intent.putExtra("livestockCategory", listings.get(position).getLivestockCategory());
        intent.putExtra("city", listings.get(position).getCity());
        intent.putExtra("state", listings.get(position).getState());

        startActivity(intent);
    }
}