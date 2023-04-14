package com.example.ecoprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity:LOG";
    public static final int DISTANCE_MAX = 10000;
    public static final int PARAM_CODE = 42;

    private SharedPreferences sharedPreferences ;

    private List<ChargerLink> listeChargeurs;
    private Map<String,Object> parametres;

    private ChargerListAdapter adapter;


    class CustomLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.v(TAG,"loc changé ?");
            Toast.makeText(MainActivity.this,"lat:" + location.getLatitude()+" long:"+location.getLongitude(),Toast.LENGTH_SHORT).show();
            boolean isLocationAlreadySet = locationUser != null;
            locationUser = location;

            if (!isLocationAlreadySet) {
                rechercheParLocation(location);
            }
        }


    }

    private void rechercheParLocation(@NonNull Location location) {
        if (calculDistanceString == null) {
            calculDistanceString = new CalculDistanceString(DISTANCE_MAX, location.getLatitude(), location.getLongitude());
        }
        else {
            calculDistanceString.setLatitude(location.getLatitude());
            calculDistanceString.setLongitude(location.getLongitude());
        }
        Log.v(TAG, calculDistanceString.getString());
        whereBuilder.setClause(calculDistanceString);
        parametres.put("where", whereBuilder.getString());
        remplirDepuisAPI(true, parametres);
    }

    private  CustomLocationListener customLocationListener;


    private static final String URL_API = "https://odre.opendatasoft.com/api/v2/catalog/datasets/bornes-irve/";

    private Retrofit retrofit;

    private final int PERMISSION_CODE_LOCATION = 100;
    private APIService service;
    private ProgressDialog chargement;

    private NetworkInfo infoReseau;

    private TextView tVTexteInfo;

    private LocationManager locationManager;
    private Location locationUser;

    private boolean isDataLoaded = true;
    private boolean isLocationEnabled = false;


    private int dernierElementAffiche = 0;
    private String sRecherche;
    private Recherche recherche;
    private CalculDistanceString calculDistanceString;
    private WhereClauseStringBuilder whereBuilder;
    private EditText eTSearch;
    private Integer unites;


    private ConnectivityManager connectivityManager;


    private Integer offset = 0;

    public static final String TABLE_NAME = "chargers";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parametres = new HashMap<>();
        recherche = new Recherche();
        whereBuilder = new WhereClauseStringBuilder();
        whereBuilder.addClause(recherche);
        sharedPreferences = getApplicationContext().getSharedPreferences(ParametresActivity.PREFERENCES_NAME,Context.MODE_PRIVATE);

        unites = sharedPreferences.getInt(ParametresActivity.UNITS_KEY,0);



        setContentView(R.layout.activity_main);
        tVTexteInfo = findViewById(R.id.emptyTextView);
        customLocationListener = new CustomLocationListener();


        listeChargeurs = new ArrayList<>();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        infoReseau = connectivityManager.getActiveNetworkInfo();

        eTSearch = findViewById(R.id.search_bar);
        eTSearch.setEnabled(false);

        DbHelper bdd = new DbHelper(this);
        SQLiteDatabase db = bdd.getWritableDatabase();

        /*
        ContentValues values = new ContentValues();
        values.put("id",1);
        db.insert("chargers",null,values);

        String[] column = {"id"};
        String[] valeurs = {"1"};
        Cursor testCursor = db.query(TABLE_NAME,column,"id=?",valeurs,null,null,"id ASC");

        if (testCursor.moveToFirst()) {
            Toast.makeText(this,"DB OK "+ testCursor.getString(testCursor.getColumnIndexOrThrow("id")),Toast.LENGTH_SHORT).show();
        }*/


        ListView lVChargeurs = findViewById(R.id.listeViewChargeurs);
        adapter = new ChargerListAdapter(this, android.R.layout.simple_list_item_1, listeChargeurs,unites);
        lVChargeurs.setAdapter(adapter);

        afficherDonnées(infoReseau);


        lVChargeurs.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                boolean isScrollingDown = lVChargeurs.getLastVisiblePosition() >= dernierElementAffiche;
                dernierElementAffiche = lVChargeurs.getLastVisiblePosition();
                if (!isScrollingDown) {
                    return;
                }

                if (lVChargeurs.getLastVisiblePosition() >= listeChargeurs.size() - 1 && isDataLoaded) {
                    infoReseau = connectivityManager.getActiveNetworkInfo();
                    if (!isConnected(infoReseau)) {
                        alertNoInternet();
                        return;
                    }
                    offset += 10;
                    Toast.makeText(MainActivity.this, R.string.data_loading, Toast.LENGTH_SHORT).show();
                    isDataLoaded = false;
                    parametres.put("offset",offset);

                    Log.v(TAG,"Appel api");
                    remplirDepuisAPI(false, parametres);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        lVChargeurs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChargerLink chargerLinkSelected = (ChargerLink) parent.getItemAtPosition(position);


                Intent detailIntent = new Intent(MainActivity.this, ChargerDetailsActivity.class);
                detailIntent.putExtra("id", chargerLinkSelected.getId());
                startActivity(detailIntent);
            }
        });

        eTSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String termeRecherche = s.toString();
                recherche.setGlobalSearchString(termeRecherche);
                parametres.put("where",whereBuilder.getString());

                sRecherche = s.toString();
                remplirDepuisAPI(true,parametres);
            }
        });

    }

    private void afficherDonnées(NetworkInfo infoReseau) {
        if (isConnected(infoReseau)) {
            parametres.remove("offset");
            remplirDepuisAPI(true,parametres);
            chargement = ProgressDialog.show(this, "",
                    getResources().getString(R.string.data_loading), true);
            chargement.show();
            tVTexteInfo.setVisibility(View.INVISIBLE);
        } else {
            alertNoInternet();
            tVTexteInfo.setVisibility(View.VISIBLE);
        }
    }


    private void getLocation() {

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            demande_permission_location();
        }
        else {
            Log.v(TAG,"location ok");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,customLocationListener);
            if (locationUser != null) {
                rechercheParLocation(locationUser);
            }
        }



    }



    private void demande_permission_location() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this).setTitle(R.string.locate_info_need)
                    .setMessage(R.string.locate_info_need)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                    {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_LOCATION);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_LOCATION);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE_LOCATION) {
            if (grantResults.length >0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"ok",Toast.LENGTH_SHORT).show();
                Log.v (TAG,"ok");
                getLocation();
            }
            else {
                new AlertDialog.Builder(this).setMessage(R.string.cant_locate).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void alertNoInternet() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(this.getString(R.string.unable_load_data))
                .setMessage(this.getString(R.string.not_connected))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void remplirDepuisAPI (boolean clear, Map<String,Object> params) {
        retrofit = new Retrofit.Builder().baseUrl(URL_API).addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(APIService.class);

        if (params.get("where") == null || params.get("where").equals("null")) {
            Log.v(TAG,"no where ?");
            params.remove("where");
        }
        else {
            Log.v(TAG,"where : "+params.get("where"));
        }

        Call<JsonResponseList> call = service.listCharger(params);
        call.enqueue(new Callback<JsonResponseList>() {
            @Override
            public void onResponse(Call<JsonResponseList> call, Response<JsonResponseList> response) {
                if (chargement.isShowing()) {
                    chargement.dismiss();
                }

                Log.v(TAG,parametres.toString());

                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"Erreur"+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }



                if (clear) {
                    listeChargeurs.clear();
                    adapter.notifyDataSetChanged();
                }

                JsonResponseList chargerList = response.body();
                if (chargerList.getChargers().isEmpty()) {
                    isDataLoaded = false;
                    return;
                }

                remplirListViewDepuisListe(chargerList);

                isDataLoaded = true;
                eTSearch.setEnabled(true);

            }

            @Override
            public void onFailure(Call<JsonResponseList> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Erreur access API", Toast.LENGTH_SHORT).show();
                tVTexteInfo.setVisibility(View.VISIBLE);
                if (chargement.isShowing()) {
                    chargement.dismiss();
                }
            }
        });
    }

    private void remplirListViewDepuisListe(JsonResponseList chargerList) {
        if (locationUser != null ) {
            for (ChargerLink chargerLink : chargerList.getChargers()) {
                chargerLink.setDistance(locationUser);
                listeChargeurs.add(chargerLink );
            }
        }
        else {
            for (ChargerLink chargerLink : chargerList.getChargers()) {
                listeChargeurs.add(chargerLink);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean isConnected (NetworkInfo networkInfo) {
        if (networkInfo == null) {
            return false;
        }
        return networkInfo.getType() == ConnectivityManager.TYPE_WIFI ||networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflaterMenu = getMenuInflater();
        inflaterMenu.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_menu_item: {
                infoReseau = connectivityManager.getActiveNetworkInfo();
                afficherDonnées(infoReseau);
                break;
            }
            case R.id.locals_chargers_item:{
                if (isLocationEnabled) {
                    Toast.makeText(this,"localisation désactivée",Toast.LENGTH_SHORT).show();
                    whereBuilder.removeClause(calculDistanceString);
                    parametres.put("where",whereBuilder.getString());
                    isLocationEnabled = false;
                    item.setIcon(R.drawable.ic_location_enabled);
                }
                else {
                    Toast.makeText(this,"localisation activée",Toast.LENGTH_SHORT).show();
                    isLocationEnabled = true;
                    item.setIcon(R.drawable.ic_location_disabled);
                    getLocation();
                }
                break;
            }
            case R.id.settings_menu_item: {
                Intent paramsIntent = new Intent(MainActivity.this, ParametresActivity.class);

                startActivityForResult(paramsIntent,PARAM_CODE);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PARAM_CODE) {
            if(resultCode == ParametresActivity.UNIT_MODIFIED_CODE){
                int result=data.getIntExtra("unit",0);
                if (unites != result) {
                        unites = result;
                        adapter.setUnite(unites);
                        adapter.notifyDataSetChanged();
                }
                Toast.makeText(MainActivity.this,"unite "+result,Toast.LENGTH_SHORT).show();
            }
        }
    }




}