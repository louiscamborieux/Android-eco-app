package com.example.ecoprojet;


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

}