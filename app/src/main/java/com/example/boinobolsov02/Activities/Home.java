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

import com.example.boinobolsov02.HelperClasses.Home.ListingsAdapter;
import com.example.boinobolsov02.HelperClasses.Home.ListingsHelper;
import com.example.boinobolsov02.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    static final float END_SCALE = 0.7f;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ImageView menuIcon, addIcon;
    LinearLayout contentView;
    RecyclerView listingsRecycler;
    RecyclerView.Adapter adapter;

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

        //Methods
        navigationDrawer();
        listingsRecycler();
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

    }

    private void listingsRecycler(){

        listingsRecycler.setHasFixedSize(true);
        listingsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<ListingsHelper> listings = new ArrayList<>();

        listings.add(new ListingsHelper(R.drawable.bois_angus, "Bois Angus", "Angus", "Pronto para Abate", "18 cabeças", "R$2000/cabeça"));
        listings.add(new ListingsHelper(R.drawable.cavalo_quarto_de_milha, "Cavalo Quarto de Milha", "Quarto de Milha", "5 anos", "1 cabeça", "R$10000"));
        listings.add(new ListingsHelper(R.drawable.ovelha_suffolk, "Ovelhas", "Suffolk", "Pronto para Abate", "6 cabeças", "R$600/cabeça"));
        listings.add(new ListingsHelper(R.drawable.porco_landrace, "Porco", "Landrace", "Pronto para Abate", "5 cabeças", "R$300/cabeça"));

        adapter = new ListingsAdapter(listings);
        listingsRecycler.setAdapter(adapter);
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
                //CHANGE TO CATEGORIES CLASS LATER
                startActivity(new Intent(getApplicationContext(), Home.class));

        }

        return true;
    }
}