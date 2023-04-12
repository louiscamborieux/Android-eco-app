package com.example.ecoprojet;


import android.location.Location;
import android.location.LocationListener;
import android.view.View;

import com.example.ecoprojet.Fields;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChargerLink {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("fields")
    @Expose
    private Fields fields;

    private Double distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public void setDistance(Location location) {
            distance =  Math.sqrt(Math.pow(location.getLatitude()-fields.getYlatitude(),2)+Math.pow(location.getLongitude()-fields.getXlongitude(),2));
            distance = CalculDistanceString.degrésToMètre(distance);
    }

    public Double getDistance() {
        return  distance;
    }

}