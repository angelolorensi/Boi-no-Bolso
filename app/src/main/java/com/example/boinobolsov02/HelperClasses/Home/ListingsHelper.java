package com.example.boinobolsov02.HelperClasses.Home;

public class ListingsHelper {

    int image;
    String title, breed, maturity, quantity, price;

    public ListingsHelper(int image, String title, String breed, String maturity, String quantity, String price) {
        this.image = image;
        this.title = title;
        this.breed = breed;
        this.maturity = maturity;
        this.quantity = quantity;
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
