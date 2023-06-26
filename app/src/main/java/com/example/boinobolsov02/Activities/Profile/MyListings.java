package com.example.boinobolsov02.Activities.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.boinobolsov02.HelperClasses.Adapters.MylistingsAdapter;

import com.example.boinobolsov02.HelperClasses.ListingsHelper;
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

public class MyListings extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    DatabaseReference database;
    ImageView goBackBtn;

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
        ArrayList<ListingsHelper> listings = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("Listings");
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        Query query = database.orderByChild("ownerId").equalTo(userId);

        //settings the adapter to the recycler
        adapter = new MylistingsAdapter(listings);
        recyclerView.setAdapter(adapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String _imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String _title = dataSnapshot.child("title").getValue(String.class);
                    String _breed = dataSnapshot.child("breed").getValue(String.class);
                    String _animalAge = dataSnapshot.child("animalAge").getValue(String.class);
                    String _quantity = dataSnapshot.child("quantity").getValue(String.class);
                    String _price = dataSnapshot.child("price").getValue(String.class);

                    ListingsHelper mylistingsHelper = new ListingsHelper(_imageUrl ,_title, _breed, _animalAge, _quantity, _price, false);

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
}