package com.example.ecoprojet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "chargeurs.db";

    public static final String SQL_CREATE = "CREATE TABLE "+MainActivity.TABLE_NAME+" (id TEXT PRIMARY KEY, amenageur TEXT," +
            "operateur TEXT, enseigne TEXT, id_station TEXT, nom_station TEXT, adresse_station TEXT, code_insee INTEGER," +
            "longitude REAL, latitude REAL, nbr_pdc INTEGER, id_pdc TEXT, puissance_max REAL, type_prise TEXT, acces_recharge TEXT," +
            "Accessibilite TEXT, observation TEXT, date_maj TEXT, source TEXT, region TEXT, departement TEXT);";
    public static final String SQL_DELETE = "DROP TABLE IF EXISTS Chargers;";

    public DbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}
