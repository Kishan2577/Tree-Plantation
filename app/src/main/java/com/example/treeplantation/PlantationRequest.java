package com.example.treeplantation;

public class PlantationRequest {
    private String placeId;

    public PlantationRequest(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}

