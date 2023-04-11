package com.example.ecoprojet;

public class CalculDistanceString extends WhereClauseElement {

    private double distance;
    private double latitude;
    private double longitude;

    public static final int QUOTIENT_DEGRE_METRE = 111139;

    public CalculDistanceString(double distance, double latitude, double longitude) {
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static double mètreToDegrés(double metre) {
        return metre /QUOTIENT_DEGRE_METRE;
    }

    public static double degrésToMètre(double degrés) {
        return  degrés* QUOTIENT_DEGRE_METRE;
    }

    public String getString() {
        return "where= ((xlongitude- "+longitude+")*(xlongitude- "+longitude+"))  -- ((ylatitude- "+latitude+")*(ylatitude- "+latitude+"))  < "+Math.pow(degrésToMètre(distance),2);
    }
}
