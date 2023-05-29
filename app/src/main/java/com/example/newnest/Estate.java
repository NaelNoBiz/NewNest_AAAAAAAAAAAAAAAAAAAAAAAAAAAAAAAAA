package com.example.newnest;

import java.util.ArrayList;

public class Estate {
    private String estateType;
    private String address;
    private String city;
    private int numOfRooms;
    private int floorNumber;
    private String picture;
    private int price;
    private String ownerId;
    private String listingType;
    private String description;
    private int size;
    private int lotSize;
    private String number;


    // For Firestore
    public Estate(){}

    public Estate(String estateType, String address, String city, int numOfRooms, int floorNumber, String picture, int price, String ownerId, String listingType, String description, int size, int lotSize, String number) {
        this.estateType = estateType;
        this.address = address;
        this.city = city;
        this.numOfRooms = numOfRooms;
        this.floorNumber = floorNumber;
        this.picture = picture;
        this.price = price;
        this.ownerId = ownerId;
        this.listingType = listingType;
        this.description = description;
        this.size = size;
        this.lotSize = lotSize;
        this.number = number;
    }

    public String getEstateType() {
        return estateType;
    }

    public void setEstateType(String estateType) {
        this.estateType = estateType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getListingType() {
        return listingType;
    }

    public void setListingType(String listingType) {
        this.listingType = listingType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Estate{" +
                "estateType='" + estateType + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", numOfRooms=" + numOfRooms +
                ", floorNumber=" + floorNumber +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", ownerId='" + ownerId + '\'' +
                ", listingType='" + listingType + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                ", lotSize=" + lotSize +
                ", number='" + number + '\'' +
                '}';
    }
}
