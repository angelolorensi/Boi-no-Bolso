package com.example.boinobolsov02.Database;

public class ListingHelper {

    String title;
    String livestockCategory;
    String animalAge;
    Integer quantity;
    String address;
    String neighborhood;
    String city;
    String cep;
    String state;
    Boolean allowSeparatedSell;

    public ListingHelper(){}

    public ListingHelper(String title, String livestockCategory, String animalAge, Integer quantity, String address, String neighborhood, String city, String cep, String state, Boolean allowSeparatedSell) {
        this.title = title;
        this.livestockCategory = livestockCategory;
        this.animalAge = animalAge;
        this.quantity = quantity;
        this.address = address;
        this.neighborhood = neighborhood;
        this.city = city;
        this.cep = cep;
        this.state = state;
        this.allowSeparatedSell = allowSeparatedSell;
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

    public String getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(String animalAge) {
        this.animalAge = animalAge;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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
}
