package com.example.boinobolsov02.Activities.Categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.boinobolsov02.Activities.Home;
import com.example.boinobolsov02.Activities.ProductPage;
import com.example.boinobolsov02.HelperClasses.Adapters.CategoriesAdapter;
import com.example.boinobolsov02.HelperClasses.Models.Listing;
import com.example.boinobolsov02.HelperClasses.RecyclerViewInterface;
import com.example.boinobolsov02.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryListings extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    DatabaseReference database;
    TextView titleTxt;
    ImageView goBackBtn;
    ArrayList<Listing> listings = new ArrayList<>();

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

        database = FirebaseDatabase.getInstance().getReference("Listings");
        String livestockCategory_ = getIntent().getStringExtra("category");
        Query query = database.orderByChild("livestockCategory").equalTo(livestockCategory_);

        adapter = new CategoriesAdapter(listings, this);
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

                    Listing categoriesHelper = new Listing(_title, _livestockCategory, _maturity, _breed, _price, _quantity, _description, _city, _state, _allowSeparatedSale, _imageUrl ,false);

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