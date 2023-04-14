package com.example.ecoprojet;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private static String TAG ="LOG:FAVORIS";
    private ChargerListAdapter adapter;
    private SharedPreferences sharedPreferences ;
    private ConnectivityManager connectivityManager;
    private NetworkInfo infoReseau;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        TextView tvError = findViewById(R.id.no_favorite);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        infoReseau = connectivityManager.getActiveNetworkInfo();




        sharedPreferences = getApplicationContext().getSharedPreferences(ParametresActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
        Integer unites = sharedPreferences.getInt(ParametresActivity.UNITS_KEY,0);

        ListView lVChargersView = findViewById(R.id.liste_chargers_favorites);
        List<ChargerLink> listeChargeurs = new ArrayList<>();
        adapter = new ChargerListAdapter(this, android.R.layout.simple_list_item_1, listeChargeurs,unites);
        lVChargersView.setAdapter(adapter);



        DbHelper bdd = new DbHelper(this);
        database= bdd.getWritableDatabase();

        Cursor cursor = database.query(MainActivity.TABLE_NAME,null,null,null,null,null,"id asc");


        if (!cursor.moveToFirst()) {
            tvError.setVisibility(View.VISIBLE);
            return;
        }
        if (cursor.isLast()) {
            return;
        }
        do {
            ChargerLink chargeur = ChargerLink.getInstance(cursor);
            listeChargeurs.add(chargeur);
            cursor.moveToNext();
        }
        while (!cursor.isLast());
        cursor.close();
        adapter.notifyDataSetChanged();


        lVChargersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChargerLink chargerLinkSelected = (ChargerLink) parent.getItemAtPosition(position);


                Intent detailIntent = new Intent(FavouritesActivity.this, ChargerDetailsActivity.class);
                detailIntent.putExtra("id", chargerLinkSelected.getId());
                infoReseau = connectivityManager.getActiveNetworkInfo();

                detailIntent.putExtra("network", MainActivity.isConnected(infoReseau));
                if (!MainActivity.isConnected(infoReseau)) {
                    detailIntent.putExtra("charger",chargerLinkSelected);
                }

                startActivity(detailIntent);
            }
        });




    }
}