package com.example.newnest;

import java.util.ArrayList;

public class User {
    private String username;
    private String userId;
    private ArrayList<String> listedEstates;
    private ArrayList<String> saved;



    public User(String username, String userId, ArrayList<String> listedEstates, ArrayList<String> saved) {
        this.username = username;
        this.listedEstates = listedEstates;
        this.saved = saved;
        this.userId = userId;
    }
    public User(){}
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getListedEstates() {
        return listedEstates;
    }

    public void setListedEstates(ArrayList<String> listedEstates) {
        this.listedEstates = listedEstates;
    }

    public ArrayList<String> getSaved() {
        return saved;
    }

    public void setSaved(ArrayList<String> saved) {
        this.saved = saved;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", listedEstates=" + listedEstates +
                ", saved=" + saved +
                '}';
    }
}
