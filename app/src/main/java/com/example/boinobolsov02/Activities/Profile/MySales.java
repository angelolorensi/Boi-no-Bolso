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
import java.util.List;
import java.util.Objects;

public class MySales extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    DatabaseReference database;
    ImageView goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sales);

        recyclerView = findViewById(R.id.mysales_recycler);
        goBackBtn = findViewById(R.id.mysales_back_btn);

        recyclerView();
        setGoBackBtn();
    }

    void recyclerView(){
        //recycler settings
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //fetching data from database
        database = FirebaseDatabase.getInstance().getReference("Listings");
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        Query query = database.orderByChild("ownerId").equalTo(userId);

        //setting the adapter to the recycler
        ArrayList<ListingsHelper> listings = new ArrayList<>();
        adapter = new MylistingsAdapter(listings);
        recyclerView.setAdapter(adapter);

        //attach a listener to retrieve the data
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String _imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String _title = dataSnapshot.child("title").getValue(String.class);
                    String _breed = dataSnapshot.child("breed").getValue(String.class);
                    String _animalAge = dataSnapshot.child("animalAge").getValue(String.class);
                    String _quantity = dataSnapshot.child("quantity").getValue(String.class);
                    Double _price = dataSnapshot.child("price").getValue(Double.class);
                    Boolean _sold = dataSnapshot.child("sold").getValue(Boolean.class);

                    ListingsHelper listingHelper = new ListingsHelper(_imageUrl ,_title, _breed, _animalAge, _quantity, _price, _sold);

                    if(listingHelper.getSold()){
                        listings.add(listingHelper);
                    }

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