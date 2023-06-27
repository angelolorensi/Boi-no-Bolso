package com.example.boinobolsov02.HelperClasses;

public class ListingsHelper {

    String imageUrl, title, breed, maturity, quantity;
    Double price;
    Boolean sold;

    public ListingsHelper(){}

    public ListingsHelper(String imageUrl, String title, String breed, String maturity, String quantity, Double price, Boolean sold) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.breed = breed;
        this.maturity = maturity;
        this.quantity = quantity;
        this.price = price;
        this.sold = sold;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
