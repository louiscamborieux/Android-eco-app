package com.example.ecoprojet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ChargerListAdapter extends ArrayAdapter<ChargerLink> {
    private Context mContext;
    private int unite;
    private static String METER_ABBR = "m";
    private static String KILOMETER_ABBR = "km";


    private static String FOOT_ABBR = "ft";
    private static String MILE_ABBR = "mi";

    public static float FOOT_METER_RATIO = 3.28084F;
    public static int FOOT_MILE_RATIO = 5280;



    private  TextView tVDistance;

    public void setUnite(int unite) {
        this.unite = unite;
    }

    public ChargerListAdapter(Context context, int resource, List<ChargerLink> objects, int unite) {
        super(context, resource, objects);
        this.unite = unite;
        mContext = context;
    }



    @Override
    public View getView(int postion, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        }

        Fields données = getItem(postion).getFields();

        TextView tVNomStation = convertView.findViewById(R.id.nomStationTextView);
        TextView tVLatitude = convertView.findViewById(R.id.latTextView);
        TextView tVLongitude = convertView.findViewById(R.id.longTextView);
        TextView tVAdresse = convertView.findViewById(R.id.adressTextView);
        TextView tVNomOperateur = convertView.findViewById(R.id.nomOperateurTextView);
        TextView tVPayant = convertView.findViewById(R.id.payantTextView);
        ImageView iVPayant = convertView.findViewById(R.id.payantImageView);

        String latAbbr = mContext.getString(R.string.lat_abbr);
        String longAbbr = mContext.getString(R.string.long_abbr);

        tVNomStation.setText(données.getnStation());
        tVLatitude.setText(latAbbr + ' '+données.getYlatitude());
        tVLongitude.setText(longAbbr + ' '+données.getXlongitude());
        tVAdresse.setText(données.getAdStation());
        tVNomOperateur.setText(données.getnOperateur());
        tVPayant.setText(données.getAccesRecharge());

        if (données.getAccesRecharge() != null && données.getAccesRecharge().equalsIgnoreCase( "payant")) {
            iVPayant.setImageResource(R.drawable.dollar_sign);
            tVPayant.setText(R.string.is_paid);
        }
        else {
            iVPayant.setImageResource(R.drawable.dollar_barre);
            tVPayant.setText(R.string.free_info);
        }

        tVDistance = convertView.findViewById(R.id.distanceTextView);
        tVDistance.setVisibility(View.INVISIBLE);
        if (getItem(postion).getDistance() != null) {
            double distance =  Math.round(getItem(postion).getDistance());


            if (unite == R.id.imperial_choice) {
                distance = Math.floor(distance* FOOT_METER_RATIO);
                tVDistance.setText(distance+FOOT_ABBR);

                if (distance > FOOT_MILE_RATIO) {
                    distance = roundPrecision(distance/FOOT_MILE_RATIO,1);
                    tVDistance.setText(distance+MILE_ABBR);
                }

            }else {
                if(distance > 1000) {
                    double distanceKM = distance/1000;
                    long distancePartieEntiere = (long)Math.floor(distanceKM);
                    long distancePartieDecimale = Math.round((distanceKM - distancePartieEntiere)*10);
                    tVDistance.setText(""+distancePartieEntiere +','+distancePartieDecimale+ KILOMETER_ABBR);
                }
                else {
                    tVDistance.setText(distance+METER_ABBR);
                }
            }


            tVDistance.setVisibility(View.VISIBLE);
        }





        return  convertView;
    }

    private static double roundPrecision (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.floor(value * scale) / scale;
    }
}

