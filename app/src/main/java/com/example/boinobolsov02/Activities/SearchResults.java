package com.example.boinobolsov02.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.boinobolsov02.HelperClasses.Adapters.ListingsAdapter;
import com.example.boinobolsov02.HelperClasses.Adapters.SearchResultsAdapter;
import com.example.boinobolsov02.HelperClasses.Models.Listing;
import com.example.boinobolsov02.HelperClasses.RecyclerViewInterface;
import com.example.boinobolsov02.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity implements RecyclerViewInterface {

    SearchView searchView;
    RecyclerView searchResultsRecycler;
    ImageView goBackBtn;
    SearchResultsAdapter adapter;
    DatabaseReference database;
    ArrayList<Listing> listings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        goBackBtn = findViewById(R.id.searchResults_go_back_icon);
        searchView = findViewById(R.id.searchResults_search_view);
        searchResultsRecycler = findViewById(R.id.search_results_recycler);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });

        setGoBackBtn();
        searchResultsRecycler();
    }

    public void setGoBackBtn() {
        goBackBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Home.class)));
    }

    private void searchResultsRecycler(){
        searchResultsRecycler.setHasFixedSize(true);
        searchResultsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        database = FirebaseDatabase.getInstance().getReference("Listings");

        database.addValueEventListener(new ValueEventListener() {
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

                    Listing listingsHelper = new Listing(_title, _livestockCategory, _maturity, _breed, _price, _quantity, _description, _city, _state, _allowSeparatedSale, _imageUrl ,false);
                    listings.add(listingsHelper);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchResults.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new SearchResultsAdapter(listings, this);
        searchResultsRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void filterList(String text){
        ArrayList<Listing> filteredList = new ArrayList<>();
        for(Listing listing : listings){
            if(listing.getTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(listing);
            }
            adapter.setFilteredList(filteredList, this);
        }
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