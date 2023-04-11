package com.example.ecoprojet;


import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {


    //Extrait de l'API
    @SerializedName("links")
    @Expose
    private List<Link> links;
    @SerializedName("record")
    @Expose
    private ChargerLink record;

    public List<Link> getLinks() {
        return links;
    }


    public ChargerLink getChargerLink() {
        return record;
    }

}
