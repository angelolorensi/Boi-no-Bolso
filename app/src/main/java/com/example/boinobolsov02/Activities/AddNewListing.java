package com.example.boinobolsov02.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boinobolsov02.HelperClasses.Models.Listing;
import com.example.boinobolsov02.HelperClasses.SessionManager;
import com.example.boinobolsov02.R;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shuhart.stepview.StepView;

import java.util.Objects;
import java.util.UUID;

public class AddNewListing extends AppCompatActivity {

    private int position = 0;

    ImageView goBackBtn, listingImage, preVisImage;
    Uri imageUri;
    StepView stepView;
    Spinner livestockTypeSpinner, maturitySpinner, stateSpinner;
    TextInputLayout title, breed, quantity, address, neighborhood, city, cep, price, description;
    Button nextBtn, finalizeBtn;
    SwitchMaterial allowSeparatedSell;
    AutoCompleteTextView cityAutocomplete, breedAutocomplete;
    TextView preVisTitle, preVisBreed, preVisAge, preVisQuantity, preVisPrice;
    RelativeLayout newListingPageOne, newListingPageTwo, newListingPageThree, newListingPageFour;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_listing);
        checkLoggedIn();

        //Hooks
        hookElements();

        //Methods
        choosePicture();
        spinners();
        goBackBtn();
        stepView();
        saveListing();
        setCityAutocomplete();
        setBreedAutocomplete();

    }

    private void checkLoggedIn(){
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        boolean isLogged = sessionManager.checkLogin();
        if (!isLogged){
            Intent intent = new Intent(getApplicationContext(), NotLoggedIn.class);
            intent.putExtra("message", "faça login para ter acesso a pagina de adição de anuncios");
            startActivity(intent);
            finish();
        }
    }

    private void saveListing() {
        finalizeBtn.setOnClickListener(view -> {
            //Get data from fields
            SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
            String _ownerId = sessionManager.getUserDetailsFromSession().get(SessionManager.KEY_PHONENUMBER);
            String _title = Objects.requireNonNull(title.getEditText()).getText().toString();
            String _breed = Objects.requireNonNull(breed.getEditText()).getText().toString();
            String _livestockCategory = livestockTypeSpinner.getSelectedItem().toString();
            String _animalAge = maturitySpinner.getSelectedItem().toString();
            String _quantity = Objects.requireNonNull(quantity.getEditText()).getText().toString();
            String _description = Objects.requireNonNull(description.getEditText()).getText().toString();
            String _address = Objects.requireNonNull(address.getEditText()).getText().toString();
            String _neighborhood = Objects.requireNonNull(neighborhood.getEditText()).getText().toString();
            String _city = Objects.requireNonNull(city.getEditText()).getText().toString();
            String _state = stateSpinner.getSelectedItem().toString();
            String _cep = Objects.requireNonNull(cep.getEditText()).getText().toString();
            String priceString = Objects.requireNonNull(price.getEditText()).getText().toString();
            Double _price = Double.parseDouble(priceString);
            Boolean _allowSeparatedSell = allowSeparatedSell.isChecked();
            String listingId = UUID.randomUUID().toString();

            //Create listing object
            Listing listingInfo = new Listing(_title, _livestockCategory, _animalAge, _breed, _price, _quantity, _description, _address, _neighborhood, _city, _cep, _state, _allowSeparatedSell, " ", _ownerId, false);
            uploadPictureAndSaveInDatabase(listingInfo, listingId);

            //redirect
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        });
    }

    private void choosePicture() {
        listingImage.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            listingImage.setImageURI(imageUri);
            preVisImage.setImageURI(imageUri);
        }
    }

    private void uploadPictureAndSaveInDatabase(Listing listing, String listingId) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Enviando imagem");

        //set image name
        final String randomKey = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/ListingsImages/" + randomKey);

        //save image at firebase storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        listing.setImageUrl(uri.toString());
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Listings");
                        reference.child(listingId).setValue(listing);
                    });
                    pd.dismiss();
                    Toast.makeText(AddNewListing.this, "Anuncio salvo com sucesso", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(AddNewListing.this, "Falha ao enviar imagem", Toast.LENGTH_SHORT).show();
                })
                .addOnProgressListener(snapshot -> {
                    double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    pd.setMessage("Progress: " + (int) progressPercent + "%");
                });
    }

    private void stepView() {
        stepView.done(false);

        nextBtn.setOnClickListener(view -> {
            switch (position) {
                case 0: {
                    if (!validateTitle() | !validateLivestock() | !validateAnimalAge() | !validateQuantity() | !validateBreed() ) {
                        return;
                    }

                    newListingPageOne.setVisibility(View.GONE);
                    newListingPageTwo.setVisibility(View.VISIBLE);
                    position = 1;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
                case 1: {
                    if (!validateAddress() | !validateNeighborhood() | !validateCity() | !validateCep() | !validateState()) {
                        return;
                    }

                    newListingPageTwo.setVisibility(View.GONE);
                    newListingPageThree.setVisibility(View.VISIBLE);
                    position = 2;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
                case 2: {
                    if (!validatePrice()) {
                        return;
                    }

                    newListingPageThree.setVisibility(View.GONE);
                    newListingPageFour.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.GONE);
                    finalizeBtn.setVisibility(View.VISIBLE);
                    setPreVisualizationContent();
                    position = 3;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
            }
        });
    }

    private void setPreVisualizationContent() {
        String _title = Objects.requireNonNull(title.getEditText()).getText().toString();
        String _breed = Objects.requireNonNull(breed.getEditText()).getText().toString();
        String _price = Objects.requireNonNull(price.getEditText()).getText().toString();
        String _animalAge = maturitySpinner.getSelectedItem().toString();
        String _quantity = Objects.requireNonNull(quantity.getEditText()).getText().toString();
        preVisTitle.setText(_title);
        preVisQuantity.setText(_quantity);
        preVisAge.setText(_animalAge);
        preVisBreed.setText(_breed);
        preVisPrice.setText(_price);
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
                case 3: {
                    newListingPageFour.setVisibility(View.GONE);
                    newListingPageThree.setVisibility(View.VISIBLE);
                    nextBtn.setVisibility(View.VISIBLE);
                    finalizeBtn.setVisibility(View.GONE);
                    position = 2;
                    stepView.done(false);
                    stepView.go(position, true);
                    break;
                }
            }
        });
    }

    private void setCityAutocomplete() {
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cities);
        cityAutocomplete.setAdapter(arrayAdapter);

    }

    private void setBreedAutocomplete() {
        String[] breeds = getResources().getStringArray(R.array.breeds);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, breeds);
        breedAutocomplete.setAdapter(arrayAdapter);

    }

    private void spinners() {

        ArrayAdapter<CharSequence> breedSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.breedSpinner, android.R.layout.simple_spinner_item);
        breedSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        livestockTypeSpinner.setAdapter(breedSpinnerAdapter);

        ArrayAdapter<CharSequence> maturitySpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.maturitySpinner, android.R.layout.simple_spinner_item);
        maturitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maturitySpinner.setAdapter(maturitySpinnerAdapter);

        ArrayAdapter<CharSequence> stateSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.ufs, android.R.layout.simple_spinner_item);
        stateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateSpinnerAdapter);

    }

    private boolean validateTitle() {

        String _title = Objects.requireNonNull(title.getEditText()).getText().toString();

        if (_title.isEmpty()) {
            title.setError("Campo título é obrigatório");
            title.requestFocus();
            return false;
        } else return true;

    }

    private boolean validateBreed() {

        String _breed = Objects.requireNonNull(breed.getEditText()).getText().toString();

        if (_breed.isEmpty()) {
            breed.setError("Campo raça é obrigatório");
            breed.requestFocus();
            return false;
        } else return true;

    }

    private boolean validatePrice() {

        String _price = Objects.requireNonNull(price.getEditText()).getText().toString();

        if (_price.isEmpty()) {
            price.setError("Campo preço é obrigatório");
            price.requestFocus();
            return false;
        } else return true;

    }

    private boolean validateLivestock() {

        String _livestockCategory = livestockTypeSpinner.getSelectedItem().toString();

        if (_livestockCategory.equals("")) {
            ((TextView) livestockTypeSpinner.getSelectedView()).setError("O campo tipo de animal não deve estar vazio!");
            return false;
        } else return true;

    }

    private boolean validateAnimalAge() {

        String _animalAge = maturitySpinner.getSelectedItem().toString();

        if (_animalAge.equals("")) {
            ((TextView) maturitySpinner.getSelectedView()).setError("O campo idade media dos animais não deve estar vazio!");
            return false;
        } else return true;

    }

    private boolean validateQuantity() {

        String _quantity = Objects.requireNonNull(quantity.getEditText()).getText().toString();

        if (_quantity.isEmpty()) {
            quantity.setError("Campo quantidade de animais é obrigatório");
            quantity.requestFocus();
            return false;
        } else return true;

    }

    private boolean validateAddress() {

        String _address = Objects.requireNonNull(address.getEditText()).getText().toString();

        if (_address.isEmpty()) {
            address.setError("Campo endereço é obrigatório");
            address.requestFocus();
            return false;
        } else return true;

    }

    private boolean validateNeighborhood() {

        String _neighborhood = Objects.requireNonNull(neighborhood.getEditText()).getText().toString();

        if (_neighborhood.isEmpty()) {
            neighborhood.setError("Campo bairro é obrigatório");
            neighborhood.requestFocus();
            return false;
        } else return true;

    }

    private boolean validateCity() {

        String _city = Objects.requireNonNull(city.getEditText()).getText().toString();

        if (_city.isEmpty()) {
            city.setError("Campo cidade é obrigatório");
            city.requestFocus();
            return false;
        } else return true;

    }

    private boolean validateCep() {

        String _cep = Objects.requireNonNull(cep.getEditText()).getText().toString();

        if (_cep.isEmpty()) {
            cep.setError("Campo cep é obrigatório");
            cep.requestFocus();
            return false;
        } else return true;

    }

    private boolean validateState() {

        String _state = stateSpinner.getSelectedItem().toString();

        if (_state.equals("Selecione um Estado")) {
            ((TextView) stateSpinner.getSelectedView()).setError("O estado não deve estar vazio!");
            return false;
        } else return true;

    }

    private void hookElements(){
        description = findViewById(R.id.new_listing_description_input_layout);
        preVisImage = findViewById(R.id.new_listing_listing_image);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        listingImage = findViewById(R.id.new_listing_image_select);
        goBackBtn = findViewById(R.id.new_listing_back_btn);
        livestockTypeSpinner = findViewById(R.id.new_listing_livestockType_spinner);
        maturitySpinner = findViewById(R.id.new_listing_maturity_spinner);
        stepView = findViewById(R.id.step_view);
        nextBtn = findViewById(R.id.new_listing_next_btn);
        finalizeBtn = findViewById(R.id.new_listing_finalize_btn);
        cityAutocomplete = findViewById(R.id.new_listing_city_autocomplete);
        breedAutocomplete = findViewById(R.id.new_listing_breed_autocomplete);
        stateSpinner = findViewById(R.id.new_listing_state_spinner);
        newListingPageOne = findViewById(R.id.new_listing_first_page);
        newListingPageTwo = findViewById(R.id.new_listing_second_page);
        newListingPageThree = findViewById(R.id.new_listing_third_page);
        newListingPageFour = findViewById(R.id.new_listing_fourth_page);
        title = findViewById(R.id.new_listing_title_input_layout);
        breed = findViewById(R.id.new_listing_breed_input_layout);
        quantity = findViewById(R.id.new_listing_quantity_input_layout);
        address = findViewById(R.id.new_listing_adress_input_layout);
        neighborhood = findViewById(R.id.new_listing_neighborhood_input_layout);
        city = findViewById(R.id.new_listing_city_input_layout);
        cep = findViewById(R.id.new_listing_city_cep_input_layout);
        price = findViewById(R.id.new_listing_price_input_layout);
        allowSeparatedSell = findViewById(R.id.new_listing_allow_separated_sell_switch);
        preVisTitle = findViewById(R.id.new_listing_listing_title);
        preVisBreed = findViewById(R.id.new_listing_listing_breed_txt);
        preVisAge = findViewById(R.id.new_listing_listing_maturity_txt);
        preVisQuantity = findViewById(R.id.new_listing_listing_quantity_txt);
        preVisPrice = findViewById(R.id.new_listing_listing_price_txt);
    }

}