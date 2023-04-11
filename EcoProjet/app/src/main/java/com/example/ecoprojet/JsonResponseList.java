package com.example.ecoprojet;

import java.util.List;
import java.util.stream.Collectors;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JsonResponseList {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("links")
    @Expose
    private List<Link> links;
    @SerializedName("records")
    @Expose
    private List<Record> records;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Record> getRecords() {
        return records;
    }

    public List<ChargerLink> getChargers() {
        return records.stream().map(Record::getChargerLink).collect(Collectors.toList());
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

}