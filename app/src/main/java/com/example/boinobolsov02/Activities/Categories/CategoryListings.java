package com.example.boinobolsov02.Activities.Categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boinobolsov02.Activities.Home;
import com.example.boinobolsov02.HelperClasses.Categories.CategoriesAdapter;
import com.example.boinobolsov02.HelperClasses.Categories.CategoriesHelper;
import com.example.boinobolsov02.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryListings extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    DatabaseReference database;
    TextView titleTxt;
    ImageView goBackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_listings);

        recyclerView = findViewById(R.id.categories_recycler);
        titleTxt = findViewById(R.id.categories_title);
        goBackBtn = findViewById(R.id.categories_back_btn);

        String livestockCategory = getIntent().getStringExtra("category");
        titleTxt.setText("Anúncios de " + livestockCategory);

        recyclerView();
        setGoBackBtn();
    }

    private void recyclerView() {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ArrayList<CategoriesHelper> listings = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("Listings");
        String livestockCategory_ = getIntent().getStringExtra("category");
        Query query = database.orderByChild("livestockCategory").equalTo(livestockCategory_);

        adapter = new CategoriesAdapter(listings);
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

                    CategoriesHelper categoriesHelper = new CategoriesHelper(_imageUrl ,_title, _breed, _animalAge, _quantity, _price);

                    listings.add(categoriesHelper);
                }
                adapter.notifyDataSetChanged();
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