package com.example.boinobolsov02.HelperClasses.Categories;

public class CategoriesHelper {

    String imageUrl, title, breed, maturity, quantity, price;

    public CategoriesHelper(){}

    public CategoriesHelper(String imageUrl, String title, String breed, String maturity, String quantity, String price) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.breed = breed;
        this.maturity = maturity;
        this.quantity = quantity;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}