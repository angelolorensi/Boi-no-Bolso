package com.example.boinobolsov02.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.boinobolsov02.R;

import java.text.NumberFormat;
import java.util.Currency;

public class ProductPage extends AppCompatActivity {

    ImageView goBackBtn, listingImage;
    TextView pageTitle, listingTitle, distance, price, paymentMethodsBtn, breed, maturity, quantity, allowSeparatedUnits, listingDescription;
    Button buyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        goBackBtn = findViewById(R.id.product_page_back_btn);
        listingImage = findViewById(R.id.product_page_image);
        pageTitle = findViewById(R.id.product_page_listing_type);
        listingTitle = findViewById(R.id.product_page_listing_title);
        distance = findViewById(R.id.product_page_listing_distance);
        paymentMethodsBtn = findViewById(R.id.product_page_listing_info_payment_methods);
        price = findViewById(R.id.product_page_listing_info_price);
        breed = findViewById(R.id.product_page_breed_txt_info);
        maturity = findViewById(R.id.product_page_maturity_txt_info);
        quantity = findViewById(R.id.product_page_quantity_txt_info);
        allowSeparatedUnits = findViewById(R.id.product_page_allowSeparated_txt);
        listingDescription = findViewById(R.id.product_page_description_info);
        buyBtn = findViewById(R.id.product_page_buy_btn);

        goBackBtn();
        loadData();
    }

    private void goBackBtn() {
        goBackBtn.setOnClickListener(view ->  startActivity(new Intent(getApplicationContext(), Home.class)));
    }

    private void loadData(){
        String breed_ = getIntent().getStringExtra("breed");
        String price_ = getIntent().getStringExtra("price");
        String maturity_ = getIntent().getStringExtra("maturity");
        String title_ = getIntent().getStringExtra("title");
        String quantity_ = getIntent().getStringExtra("quantity");
        String imageUrl_ = getIntent().getStringExtra("imageUrl");

        //format price
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
        priceFormat.setMaximumFractionDigits(0);
        priceFormat.setCurrency(Currency.getInstance("BRL"));
        priceFormat.format(price_);
        price.setText(price_);

        breed.setText(breed_);
        maturity.setText(maturity_);
        listingTitle.setText(title_);
        quantity.setText(quantity_);
        Glide.with(this).load(imageUrl_).into(listingImage);

    }
}