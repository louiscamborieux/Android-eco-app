package com.example.ecoprojet;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JsonResponseRecord {

    @SerializedName("links")
    @Expose
    private List<Link> links;
    @SerializedName("record")
    @Expose
    private ChargerLink chargerLink;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public ChargerLink getChargerLink() {
        return chargerLink;
    }

    public void setChargerLink(ChargerLink chargerLink) {
        this.chargerLink = chargerLink;
    }
}