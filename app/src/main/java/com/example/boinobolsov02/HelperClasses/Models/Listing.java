package com.example.boinobolsov02.HelperClasses.Models;

public class Listing {

    String title;
    String livestockCategory;
    String maturity;
    String breed;
    Double price;
    String quantity;
    String description;
    String address;
    String neighborhood;
    String city;
    String cep;
    String state;
    Boolean allowSeparatedSell;
    String imageUrl;
    String ownerId;
    Boolean sold;

    public Listing() {
    }

    public Listing(String title, String livestockCategory, String maturity, String breed, Double price, String quantity, String description, String address, String neighborhood, String city, String cep, String state, Boolean allowSeparatedSell, String imageUrl, String ownerId, Boolean sold) {
        this.title = title;
        this.livestockCategory = livestockCategory;
        this.maturity = maturity;
        this.breed = breed;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.address = address;
        this.neighborhood = neighborhood;
        this.city = city;
        this.cep = cep;
        this.state = state;
        this.allowSeparatedSell = allowSeparatedSell;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.sold = sold;
    }

    public Listing(String title, String livestockCategory, String maturity, String breed, Double price, String quantity, String description, String city, String state, Boolean allowSeparatedSell, String imageUrl, Boolean sold){
        this.title = title;
        this.livestockCategory = livestockCategory;
        this.maturity = maturity;
        this.breed = breed;
        this.price = price;
        this.city = city;
        this.quantity = quantity;
        this.state = state;
        this.description = description;
        this.allowSeparatedSell = allowSeparatedSell;
        this.imageUrl = imageUrl;
        this.sold = sold;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLivestockCategory() {
        return livestockCategory;
    }

    public void setLivestockCategory(String livestockCategory) {
        this.livestockCategory = livestockCategory;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getAllowSeparatedSell() {
        return allowSeparatedSell;
    }

    public void setAllowSeparatedSell(Boolean allowSeparatedSell) {
        this.allowSeparatedSell = allowSeparatedSell;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}