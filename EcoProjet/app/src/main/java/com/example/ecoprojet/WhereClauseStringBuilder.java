package com.example.ecoprojet;

import java.util.Iterator;
import java.util.List;

public class WhereClauseStringBuilder {

    private List<WhereClauseElement> whereClauseElementList;

    public WhereClauseStringBuilder(List<WhereClauseElement> liste) {
        whereClauseElementList = liste;
    }

    public String getString() {
        String res = "";
        Iterator<WhereClauseElement> iterateur = whereClauseElementList.iterator();
        if (iterateur.hasNext()) {
            res += iterateur.next().getString();
        }
        while (iterateur.hasNext()) {
            res+= " and "+iterateur.next();
        }
        return res;
    }

}
