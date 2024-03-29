package com.example.boinobolsov02.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.boinobolsov02.Activities.Categories.CategoryListings;
import com.example.boinobolsov02.Activities.LoginSignup.Login;
import com.example.boinobolsov02.Activities.Profile.UserProfile;
import com.example.boinobolsov02.HelperClasses.Adapters.ListingsAdapter;
import com.example.boinobolsov02.HelperClasses.Models.Listing;
import com.example.boinobolsov02.HelperClasses.RecyclerViewInterface;
import com.example.boinobolsov02.HelperClasses.SessionManager;
import com.example.boinobolsov02.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewInterface {

    static final float END_SCALE = 0.7f;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView menuIcon, addIcon;
    RelativeLayout searchBar,bovinosIcon, equinosIcon, suinosIcon, ovinosIcon;
    LinearLayout contentView;
    RecyclerView listingsRecycler;
    RecyclerView.Adapter adapter;
    DatabaseReference database;
    ArrayList<Listing> listings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hooks
        searchBar = findViewById(R.id.home_search_Bar);
        navigationView = findViewById(R.id.home_navigation_view);
        drawerLayout = findViewById(R.id.home_drawer_layout);
        addIcon = findViewById(R.id.home_add_icon);
        menuIcon = findViewById(R.id.home_menu_icon);
        contentView = findViewById(R.id.home_content);
        listingsRecycler = findViewById(R.id.home_listings_recycler);
        bovinosIcon = findViewById(R.id.bovinos_category_btn);
        equinosIcon = findViewById(R.id.equinos_category_btn);
        suinosIcon = findViewById(R.id.suinos_category_btn);
        ovinosIcon = findViewById(R.id.ovinos_category_btn);

        //Methods
        navigationDrawer();
        listingsRecycler();
        callCategoriesScreen();
        search();
    }

    private void search() {
        searchBar.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SearchResults.class)));
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(view -> {
            if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
        callAddListingScreen();
    }

    private void listingsRecycler(){

        listingsRecycler.setHasFixedSize(true);
        listingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        database = FirebaseDatabase.getInstance().getReference("Listings");

        adapter = new ListingsAdapter(listings, this);
        listingsRecycler.setAdapter(adapter);

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
            adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateNavigationDrawer() {

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), Home.class));
                break;
            case R.id.nav_cow:
                Intent intent = new Intent(getApplicationContext(), CategoryListings.class);
                intent.putExtra("category", "Bovinos");
                startActivity(intent);
                break;
            case R.id.nav_horse:
                Intent intent2 = new Intent(getApplicationContext(), CategoryListings.class);
                intent2.putExtra("category", "Equinos");
                startActivity(intent2);
                break;
            case R.id.nav_pig:
                Intent intent3 = new Intent(getApplicationContext(), CategoryListings.class);
                intent3.putExtra("category", "Suínos");
                startActivity(intent3);
                break;
            case R.id.nav_sheep:
                Intent intent4 = new Intent(getApplicationContext(), CategoryListings.class);
                intent4.putExtra("category", "Ovinos");
                startActivity(intent4);
                break;
            case R.id.nav_profile:
                Intent intent5 = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent5);
                break;
            case R.id.nav_logout:
                SessionManager session = new SessionManager(Home.this, SessionManager.SESSION_USERSESSION);
                SessionManager rememberMe = new SessionManager(Home.this, SessionManager.SESSION_REMEMBERME);
                session.logoutUserFromSession();
                rememberMe.logoutUserFromSession();
                Intent intent6 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent6);
                break;
        }

        return true;
    }

    void callAddListingScreen(){
        addIcon.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), AddNewListing.class)));
    }

    void callCategoriesScreen(){
        bovinosIcon.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CategoryListings.class);
            intent.putExtra("category", "Bovinos");
            startActivity(intent);
        });

        equinosIcon.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CategoryListings.class);
            intent.putExtra("category", "Equinos");
            startActivity(intent);
        });

        suinosIcon.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CategoryListings.class);
            intent.putExtra("category", "Suínos");
            startActivity(intent);
        });

        ovinosIcon.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CategoryListings.class);
            intent.putExtra("category", "Ovinos");
            startActivity(intent);
        });
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