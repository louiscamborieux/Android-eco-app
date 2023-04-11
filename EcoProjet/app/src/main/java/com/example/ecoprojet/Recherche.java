package com.example.ecoprojet;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Recherche extends WhereClauseElement {

    //Recherche par cle-valeur dans la clause Where
    final String TAG = "LOG:RECHERCHE";

    private Map<String,String> termes;
    private String globalSearchString;

    public Recherche() {
        this.termes = new HashMap<>();
    }

    public void addTerme(String champ,String valeur) {
        this.termes.put(champ,valeur);
    }

    public void removeTerme(String champ) {
        this.termes.remove(champ);
    }

    public void setGlobalSearchString(String globalSearchString) {
        this.globalSearchString = globalSearchString;
    }

    public String getString() {
        String res = "";
        Iterator<String>  ittermes = termes.keySet().iterator();

        if (globalSearchString == null ||globalSearchString.equals("")) {
            if(!ittermes.hasNext()) {
                return null;
            }
            String terme = ittermes.next();
            res = "search("+terme+",'"+termes.get(terme)+"')";
        }
        else {
            res = "search('"+globalSearchString+"')";
        }
        while (ittermes.hasNext()) {
            String terme = ittermes.next();
            res+=" and search("+terme+",'"+termes.get(terme)+"')";
        }
        Log.v(TAG,res);
        return  res;
    }
}
