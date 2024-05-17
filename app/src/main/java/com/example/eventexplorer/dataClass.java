package com.example.eventexplorer;

public class dataClass {
    String name;
    String name2;
    String email;
    String address1;
    String address2;
    String landmark;
    String town;

    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getTown() {
        return town;
    }

    public String getState() {
        return state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getImageURL() {
        return imageURL;
    }

    String state;
    String pinCode;
    String imageURL;

    public dataClass(String name, String name2, String email, String address1, String address2, String landmark, String town, String state, String pinCode, String imageURL) {
        this.name = name;
        this.name2 = name2;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.landmark = landmark;
        this.town = town;
        this.state = state;
        this.pinCode = pinCode;
        this.imageURL = imageURL;
    }

    // Getters and setters...
}
