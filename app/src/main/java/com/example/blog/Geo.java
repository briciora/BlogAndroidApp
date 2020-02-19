package com.example.blog;

import com.google.gson.annotations.SerializedName;

public class Geo {

    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;

    public Geo(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(Geo geo) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(Geo geo) {
        this.lng = lng;
    }

}