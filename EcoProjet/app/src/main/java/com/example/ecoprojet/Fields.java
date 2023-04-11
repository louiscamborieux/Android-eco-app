package com.example.ecoprojet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("n_amenageur")
    @Expose
    private String nAmenageur;
    @SerializedName("n_operateur")
    @Expose
    private String nOperateur;
    @SerializedName("n_enseigne")
    @Expose
    private String nEnseigne;
    @SerializedName("id_station")
    @Expose
    private String idStation;
    @SerializedName("n_station")
    @Expose
    private String nStation;
    @SerializedName("ad_station")
    @Expose
    private String adStation;
    @SerializedName("code_insee")
    @Expose
    private Integer codeInsee;
    @SerializedName("xlongitude")
    @Expose
    private Double xlongitude;
    @SerializedName("ylatitude")
    @Expose
    private Double ylatitude;
    @SerializedName("nbre_pdc")
    @Expose
    private Integer nbrePdc;
    @SerializedName("id_pdc")
    @Expose
    private String idPdc;
    @SerializedName("puiss_max")
    @Expose
    private Double puissMax;
    @SerializedName("type_prise")
    @Expose
    private String typePrise;
    @SerializedName("acces_recharge")
    @Expose
    private String accesRecharge;
    @SerializedName("accessibilite")
    @Expose
    private String accessibilite;
    @SerializedName("observations")
    @Expose
    private String observations;
    @SerializedName("date_maj")
    @Expose
    private String dateMaj;
    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("code_insee_commune")
    @Expose
    private String codeInseeCommune;
    @SerializedName("region")
    @Expose
    private Object region;
    @SerializedName("departement")
    @Expose
    private String departement;

    public String getnAmenageur() {
        return nAmenageur;
    }

    public void setnAmenageur(String nAmenageur) {
        this.nAmenageur = nAmenageur;
    }

    public String getnOperateur() {
        return nOperateur;
    }

    public void setnOperateur(String nOperateur) {
        this.nOperateur = nOperateur;
    }

    public String getnEnseigne() {
        return nEnseigne;
    }

    public void setnEnseigne(String nEnseigne) {
        this.nEnseigne = nEnseigne;
    }

    public String getIdStation() {
        return idStation;
    }

    public void setIdStation(String idStation) {
        this.idStation = idStation;
    }

    public String getnStation() {
        return nStation;
    }

    public void setnStation(String nStation) {
        this.nStation = nStation;
    }

    public String getAdStation() {
        return adStation;
    }

    public void setAdStation(String adStation) {
        this.adStation = adStation;
    }

    public Integer getCodeInsee() {
        return codeInsee;
    }

    public void setCodeInsee(Integer codeInsee) {
        this.codeInsee = codeInsee;
    }

    public Double getXlongitude() {
        return xlongitude;
    }

    public void setXlongitude(Double xlongitude) {
        this.xlongitude = xlongitude;
    }

    public Double getYlatitude() {
        return ylatitude;
    }

    public void setYlatitude(Double ylatitude) {
        this.ylatitude = ylatitude;
    }

    public Integer getNbrePdc() {
        return nbrePdc;
    }

    public void setNbrePdc(Integer nbrePdc) {
        this.nbrePdc = nbrePdc;
    }

    public String getIdPdc() {
        return idPdc;
    }

    public void setIdPdc(String idPdc) {
        this.idPdc = idPdc;
    }

    public Double getPuissMax() {
        return puissMax;
    }

    public void setPuissMax(Double puissMax) {
        this.puissMax = puissMax;
    }

    public String getTypePrise() {
        return typePrise;
    }

    public void setTypePrise(String typePrise) {
        this.typePrise = typePrise;
    }

    public String getAccesRecharge() {
        return accesRecharge;
    }

    public void setAccesRecharge(String accesRecharge) {
        this.accesRecharge = accesRecharge;
    }

    public String getAccessibilite() {
        return accessibilite;
    }

    public void setAccessibilite(String accessibilite) {
        this.accessibilite = accessibilite;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDateMaj() {
        return dateMaj;
    }

    public void setDateMaj(String dateMaj) {
        this.dateMaj = dateMaj;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getCodeInseeCommune() {
        return codeInseeCommune;
    }

    public void setCodeInseeCommune(String codeInseeCommune) {
        this.codeInseeCommune = codeInseeCommune;
    }

    public Object getRegion() {
        return region;
    }

    public void setRegion(Object region) {
        this.region = region;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

}
