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
import com.example.boinobolsov02.HelperClasses.Home.ListingsAdapter;
import com.example.boinobolsov02.HelperClasses.Home.ListingsHelper;
import com.example.boinobolsov02.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static final float END_SCALE = 0.7f;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView menuIcon, addIcon;
    RelativeLayout bovinosIcon, equinosIcon, suinosIcon, ovinosIcon;
    LinearLayout contentView;
    RecyclerView listingsRecycler;
    RecyclerView.Adapter adapter;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Hooks
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
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();
        callAddListingScreen();
    }

    private void listingsRecycler(){

        listingsRecycler.setHasFixedSize(true);
        listingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<ListingsHelper> listings = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("Listings");

        adapter = new ListingsAdapter(listings);
        listingsRecycler.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    String _imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                    String _title = dataSnapshot.child("title").getValue(String.class);
                    String _breed = dataSnapshot.child("breed").getValue(String.class);
                    String _animalAge = dataSnapshot.child("animalAge").getValue(String.class);
                    String _quantity = dataSnapshot.child("quantity").getValue(String.class);
                    String _price = dataSnapshot.child("price").getValue(String.class);

                    ListingsHelper listingsHelper = new ListingsHelper(_imageUrl ,_title, _breed, _animalAge, _quantity, _price);

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

            case R.id.nav_categories:
                startActivity(new Intent(getApplicationContext(), CategoryListings.class));
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
}