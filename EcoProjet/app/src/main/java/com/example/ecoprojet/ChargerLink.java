package com.example.ecoprojet;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ecoprojet.Fields;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ChargerLink implements Parcelable {

    public static final int FAVORITE_ADDED_CODE = 0;
    public static final int FAVORITE_REMOVED_CODE = 1;


    private ChargerLink() {

    }

    protected ChargerLink(Parcel in) {
        fields = new Fields();

        id = in.readString();
        fields.setnEnseigne(in.readString());
        fields.setnAmenageur(in.readString());
        fields.setnOperateur(in.readString());
        fields.setAccesRecharge(in.readString());
        fields.setAccessibilite(in.readString());
        fields.setRegion(in.readString());
        fields.setAdStation(in.readString());
        fields.setCodeInsee(in.readInt());
        fields.setXlongitude(in.readDouble());
        fields.setYlatitude(in.readDouble());
        fields.setDateMaj(in.readString());
        fields.setDepartement(in.readString());
        fields.setPuissMax(in.readDouble());
        fields.setNbrePdc(in.readInt());
        fields.setTypePrise(in.readString());
        fields.setSource(in.readString());
        fields.setObservations(in.readString());
        fields.setIdPdc(in.readString());

    }

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

    public static ChargerLink getInstance(Cursor cursor) {
        ChargerLink res = new ChargerLink();
        Fields fields = new Fields();
        res.setFields(fields);

        res.setId( cursor.getString(cursor.getColumnIndexOrThrow("id")));
        fields.setnAmenageur(cursor.getString(cursor.getColumnIndexOrThrow("amenageur")));
        fields.setnOperateur(cursor.getString(cursor.getColumnIndexOrThrow("operateur")));
        fields.setnEnseigne(cursor.getString(cursor.getColumnIndexOrThrow("enseigne")));
        fields.setIdStation(cursor.getString(cursor.getColumnIndexOrThrow("id_station")));
        fields.setnStation(cursor.getString(cursor.getColumnIndexOrThrow("nom_station")));
        fields.setAdStation(cursor.getString(cursor.getColumnIndexOrThrow("adresse_station")));
        fields.setCodeInsee(cursor.getInt(cursor.getColumnIndexOrThrow("code_insee")));
        fields.setXlongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
        fields.setYlatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
        fields.setNbrePdc(cursor.getInt(cursor.getColumnIndexOrThrow("nbr_pdc")));
        fields.setIdPdc(cursor.getString(cursor.getColumnIndexOrThrow("id_pdc")));
        fields.setPuissMax(cursor.getDouble(cursor.getColumnIndexOrThrow("puissance_max")));
        fields.setTypePrise(cursor.getString(cursor.getColumnIndexOrThrow("type_prise")));
        fields.setAccesRecharge(cursor.getString(cursor.getColumnIndexOrThrow("acces_recharge")));
        fields.setAccessibilite(cursor.getString(cursor.getColumnIndexOrThrow("Accessibilite")));
        fields.setObservations(cursor.getString(cursor.getColumnIndexOrThrow("observation")));
        fields.setDateMaj(cursor.getString(cursor.getColumnIndexOrThrow("date_maj")));
        fields.setSource(cursor.getString(cursor.getColumnIndexOrThrow("source")));
        fields.setRegion(cursor.getString(cursor.getColumnIndexOrThrow("region")));
        fields.setDepartement(cursor.getString(cursor.getColumnIndexOrThrow("departement")));

        return  res;
    }

    public  int toggleFavorite (SQLiteDatabase database) {
        if (!isFavorite(database)) {
            insertInstance(database);
            return FAVORITE_ADDED_CODE;
        }
        else {
            removeInstance(database);
            return FAVORITE_REMOVED_CODE;
        }
    }

    private void removeInstance(SQLiteDatabase database) {

        String args[] = {getId()};
        database.delete(MainActivity.TABLE_NAME, "id=?",args);

    }

    private void insertInstance (SQLiteDatabase database) {

        ContentValues values = new ContentValues();
        values.put("id", getId());
        values.put("id_station", fields.getIdStation());
        values.put("amenageur", fields.getnAmenageur());
        values.put("operateur",fields.getnOperateur());
        values.put("enseigne",fields.getnEnseigne());
        values.put("nom_station", fields.getnStation());
        values.put("adresse_station", fields.getAdStation());
        values.put("code_insee", fields.getCodeInsee());
        values.put("longitude", fields.getXlongitude());
        values.put("latitude", fields.getYlatitude());
        values.put("nbr_pdc", fields.getNbrePdc());
        values.put("id_pdc", fields.getIdPdc());
        values.put("puissance_max", fields.getPuissMax());
        values.put("type_prise", fields.getTypePrise());
        values.put("acces_recharge", fields.getAccesRecharge());
        values.put("Accessibilite", fields.getAccessibilite());
        values.put("observation", fields.getObservations());
        values.put("date_maj", fields.getDateMaj());
        values.put("source", fields.getSource ());
        values.put("region", fields.getRegion());
        values.put("departement", fields.getDepartement());

        database.insert(MainActivity.TABLE_NAME,null,values);

    }

    public boolean isFavorite (SQLiteDatabase database) {

        String[] columns = {"id"};
        String[] valeurs = {getId()};
        Cursor cursor = database.query(MainActivity.TABLE_NAME,columns,columns[0]+"=?",valeurs,null,null,"id asc");

        return  cursor.moveToFirst();


    }

    @Override
    public String toString() {
        return "ChargerLink{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", size=" + size +
                ", fields=" + fields +
                ", distance=" + distance +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fields.getnEnseigne());
        dest.writeString(fields.getnAmenageur());
        dest.writeString(fields.getnOperateur());
        dest.writeString(fields.getAccesRecharge());
        dest.writeString(fields.getAccessibilite());
        dest.writeString(fields.getRegion());
        dest.writeString(fields.getAdStation());
        dest.writeInt(fields.getCodeInsee());
        dest.writeDouble(fields.getXlongitude());
        dest.writeDouble(fields.getYlatitude());
        dest.writeString(fields.getDateMaj());
        dest.writeString(fields.getDepartement());
        dest.writeDouble(fields.getPuissMax());
        dest.writeInt(fields.getNbrePdc());
        dest.writeString(fields.getTypePrise());
        dest.writeString(fields.getSource());
        dest.writeString(fields.getObservations());
        dest.writeString(fields.getIdPdc());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ChargerLink> CREATOR = new Parcelable.Creator<ChargerLink>() {
        public ChargerLink createFromParcel(Parcel in) {
            return new ChargerLink(in);
        }

        public ChargerLink[] newArray(int size) {
            return new ChargerLink[size];
        }
    };
}