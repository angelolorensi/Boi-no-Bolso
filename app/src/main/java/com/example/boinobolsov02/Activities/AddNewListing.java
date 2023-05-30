package com.example.boinobolsov02.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.boinobolsov02.R;

import com.shuhart.stepview.StepView;

public class AddNewListing extends AppCompatActivity {

    private int position = 0;

    ImageView goBackBtn;
    StepView stepView;
    Spinner breedSpinner, maturitySpinner, stateSpinner;
    Button nextBtn;
    AutoCompleteTextView cityAutocomplete;
    RelativeLayout newListingPageOne, newListingPageTwo, newListingPageThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_listing);

        //Hooks
        goBackBtn = findViewById(R.id.new_listing_back_btn);
        breedSpinner = findViewById(R.id.new_listing_breed_spinner);
        maturitySpinner = findViewById(R.id.new_listing_maturity_spinner);
        stepView = findViewById(R.id.step_view);
        nextBtn = findViewById(R.id.new_listing_next_btn);
        cityAutocomplete = findViewById(R.id.new_listing_city_autocomplete);
        stateSpinner = findViewById(R.id.new_listing_state_spinner);
        newListingPageOne = findViewById(R.id.new_listing_first_page);
        newListingPageTwo = findViewById(R.id.new_listing_second_page);
        newListingPageThree = findViewById(R.id.new_listing_third_page);

        //Methods
        spinners();
        goBackBtn();
        stepView();
        setCityAutocomplete();
    }

    private void stepView() {
        stepView.done(false);

        nextBtn.setOnClickListener(view -> {
            switch (position) {
                case 0: {
                    newListingPageOne.setVisibility(View.GONE);
                    newListingPageTwo.setVisibility(View.VISIBLE);
                    position = 1;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
                case 1: {
                    newListingPageTwo.setVisibility(View.GONE);
                    newListingPageThree.setVisibility(View.VISIBLE);
                    position = 2;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
            }
        });
    }

    private void goBackBtn() {
        goBackBtn.setOnClickListener(view -> {
            switch (position) {
                case 0: {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    break;
                }
                case 1: {
                    newListingPageTwo.setVisibility(View.GONE);
                    newListingPageOne.setVisibility(View.VISIBLE);
                    position = 0;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
                case 2: {
                    newListingPageThree.setVisibility(View.GONE);
                    newListingPageTwo.setVisibility(View.VISIBLE);
                    position = 1;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
            }
        });
    }

    private void setCityAutocomplete() {
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        cityAutocomplete.setAdapter(arrayAdapter);

    }


    private void spinners() {

        ArrayAdapter<CharSequence> breedSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.breedSpinner, android.R.layout.simple_spinner_item);

        breedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        breedSpinner.setAdapter(breedSpinnerAdapter);

        ArrayAdapter<CharSequence> maturitySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.maturitySpinner, android.R.layout.simple_spinner_item);

        maturitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        maturitySpinner.setAdapter(maturitySpinnerAdapter);

        ArrayAdapter<CharSequence> stateSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ufs, android.R.layout.simple_spinner_item);

        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(stateSpinnerAdapter);

    }

}