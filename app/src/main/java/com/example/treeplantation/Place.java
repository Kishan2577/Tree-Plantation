package com.example.treeplantation;

import java.io.Serializable;

public class Place implements Serializable {
    private String id;
    private String name;
    private String description;
    private String state;
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    // Constructors, getters, and setters
    public Place(String id,String name, String description, String state, String city, String country, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getId(){return id;}
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}
