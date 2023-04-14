package com.example.ecoprojet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ParametresActivity extends AppCompatActivity {

    private RadioButton radioButton;
    public static int UNIT_MODIFIED_CODE = 1;
    public static String UNITS_KEY = "units";

    public static String TAG = "LOG:PARAMETRES";
    public static String PREFERENCES_NAME = "settings";


    private Button saveButton;
    private SharedPreferences sharedPreferences;

    private Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        returnIntent = new Intent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tVInfoUnites = findViewById(R.id.unit_choice);
        RadioGroup radioGroupUnites = findViewById(R.id.unit_choice_rg);
        saveButton = findViewById(R.id.save_button);

        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

        Integer unites = sharedPreferences.getInt(ParametresActivity.UNITS_KEY,0);


        if (unites != null && unites != 0) {
            radioButton = findViewById(unites);
        }
        else {
            radioButton = findViewById(R.id.metric_choice);
        }
        radioButton.setChecked(true);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedButton = radioGroupUnites.getCheckedRadioButtonId();

                radioButton = (RadioButton) findViewById(selectedButton);
                if (radioButton == null) {
                    finish();
                    return;
                }

                returnIntent.putExtra("unit",selectedButton);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(UNITS_KEY,selectedButton);
                editor.commit();

                setResult(UNIT_MODIFIED_CODE,returnIntent);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}