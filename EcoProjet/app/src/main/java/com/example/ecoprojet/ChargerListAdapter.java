package com.example.ecoprojet;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ChargerListAdapter extends ArrayAdapter<ChargerLink> {
    private Context mContext;

    private Location location;

    private  TextView tVDistance;

    public ChargerListAdapter(Context context, int resource,  List<ChargerLink> objects, Location location) {
        super(context, resource, objects);
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
        }
        else {
            iVPayant.setImageResource(R.drawable.dollar_barre);
        }

        tVDistance = convertView.findViewById(R.id.distanceTextView);
        tVDistance.setVisibility(View.INVISIBLE);
        if (getItem(postion).getDistance() != null) {
            tVDistance.setText(Math.round(getItem(postion).getDistance())+ "m");
            tVDistance.setVisibility(View.VISIBLE);
        }





        return  convertView;
    }
}

