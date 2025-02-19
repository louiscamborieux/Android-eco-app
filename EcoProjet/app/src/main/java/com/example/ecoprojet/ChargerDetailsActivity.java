package com.example.ecoprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import kotlin.jvm.internal.Ref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChargerDetailsActivity extends AppCompatActivity {
    static final String URL_API = "https://odre.opendatasoft.com/api/v2/catalog/datasets/bornes-irve/records/";

    static final String MAP_URI = "geo:";
    private Double latitude;
    private Double longitude;

    private ChargerLink chargerLink;

    private SQLiteDatabase database;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DbHelper bdd = new DbHelper(ChargerDetailsActivity.this);
        database= bdd.getWritableDatabase();

        Bundle extras = getIntent().getExtras();

        Retrofit retrofit  = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL_API).build();
        APIService service = retrofit.create(APIService.class);

        boolean isConnected = extras.getBoolean("network");


        TextView tVOpérateur = findViewById(R.id.operateur_data);
        TextView tVAccès = findViewById(R.id.acces_data);
        TextView tVDispo = findViewById(R.id.dispo_data);
        TextView tVAmenageur = findViewById(R.id.amenageur_data);
        TextView tVEnseigne = findViewById(R.id.enseigne_data);
        TextView tVMAJ = findViewById(R.id.maj_data);
        TextView tVPdc = findViewById(R.id.nb_pdc_data);
        TextView tVObservations = findViewById(R.id.observations_data);
        TextView tVPuissanceMax = findViewById(R.id.puiss_max_data);
        TextView tVSource = findViewById(R.id.source_data);
        TextView tVTypePrise = findViewById(R.id.type_prise_data);
        TextView tVAdresse = findViewById(R.id.adresse);

        Button buttonItineraire = findViewById(R.id.itinerary_button);
        Button buttonFavorite = findViewById(R.id.favorite_button);


        if (isConnected) {
            Call<JsonResponseRecord> call = service.chargeur(URL_API + extras.getString("id"));

            call.enqueue(new Callback<JsonResponseRecord>() {
                @Override
                public void onResponse(Call<JsonResponseRecord> call, Response<JsonResponseRecord> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(ChargerDetailsActivity.this, "Erreur" + response.code(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    JsonResponseRecord reponse = response.body();
                    chargerLink = reponse.getChargerLink();

                    if (chargerLink == null) {
                        Toast.makeText(ChargerDetailsActivity.this, "données_vides", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Fields champs = chargerLink.getFields();
                    setTitle(champs.getnStation());
                    tVEnseigne.setText(champs.getnEnseigne());
                    tVAccès.setText(champs.getAccesRecharge());
                    tVAmenageur.setText(champs.getnAmenageur());
                    tVOpérateur.setText(champs.getnOperateur());
                    tVMAJ.setText(champs.getDateMaj());
                    tVObservations.setText(champs.getObservations());
                    tVPdc.setText(champs.getNbrePdc().toString());
                    tVPuissanceMax.setText(champs.getPuissMax() + " kW");
                    tVSource.setText(champs.getSource());
                    tVTypePrise.setText(champs.getTypePrise());
                    tVDispo.setText(champs.getAccessibilite());
                    latitude = champs.getYlatitude();
                    longitude = champs.getXlongitude();

                    if (chargerLink.isFavorite(database)) {
                        buttonFavorite.setText(R.string.remove_favorite);
                    }


                }
                @Override
                public void onFailure(Call<JsonResponseRecord> call, Throwable t) {
                    Toast.makeText(ChargerDetailsActivity.this, "Erreur access API", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            chargerLink = extras.getParcelable("charger");

            Fields champs = chargerLink.getFields();
            setTitle(champs.getnStation());
            tVEnseigne.setText(champs.getnEnseigne());
            tVAccès.setText(champs.getAccesRecharge());
            tVAmenageur.setText(champs.getnAmenageur());
            tVOpérateur.setText(champs.getnOperateur());
            tVMAJ.setText(champs.getDateMaj());
            tVObservations.setText(champs.getObservations());
            tVPdc.setText(champs.getNbrePdc().toString());
            tVPuissanceMax.setText(champs.getPuissMax() + " kW");
            tVSource.setText(champs.getSource());
            tVTypePrise.setText(champs.getTypePrise());
            tVDispo.setText(champs.getAccessibilite());
            latitude = champs.getYlatitude();
            longitude = champs.getXlongitude();

            if (chargerLink.isFavorite(database)) {
                buttonFavorite.setText(R.string.remove_favorite);
            }

        }





        buttonItineraire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coordinates = "geo:"+latitude+","+longitude+"?q="+latitude+","+longitude;

                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse(coordinates) );
                startActivity( intent );
                Toast.makeText(ChargerDetailsActivity.this,"ok",Toast.LENGTH_SHORT).show();
            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int code = chargerLink.toggleFavorite(database);
                switch (code) {
                    case ChargerLink.FAVORITE_ADDED_CODE: {
                        Toast.makeText(ChargerDetailsActivity.this,R.string.favorite_added_text, Toast.LENGTH_SHORT).show();
                        buttonFavorite.setText(R.string.remove_favorite);
                        break;}
                    case ChargerLink.FAVORITE_REMOVED_CODE: {
                        Toast.makeText(ChargerDetailsActivity.this,R.string.favorite_removed_text,Toast.LENGTH_SHORT).show();
                        buttonFavorite.setText(R.string.add_favorite);
                        break;
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return  super.onOptionsItemSelected(item);
    }
}