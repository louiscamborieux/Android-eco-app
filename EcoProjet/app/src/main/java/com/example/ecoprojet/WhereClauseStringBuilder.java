package com.example.ecoprojet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WhereClauseStringBuilder {

    private List<WhereClauseElement> whereClauseElementList;

    public WhereClauseStringBuilder(List<WhereClauseElement> liste) {
        whereClauseElementList = liste;
    }

    public WhereClauseStringBuilder() {
        whereClauseElementList = new ArrayList<>();
    }

    public String getString() {
        String res = "";
        Iterator<WhereClauseElement> iterateur = whereClauseElementList.iterator();
        String courant;
        if (iterateur.hasNext()) {
            courant = iterateur.next().getString();
            while (courant == null && iterateur.hasNext()) {
                courant = iterateur.next().getString();
            }
            res += courant;
        }
        while (iterateur.hasNext()) {
            res+= " and "+iterateur.next().getString();
        }
        return res;
    }

    public void addClause(WhereClauseElement element) {
        whereClauseElementList.add(element);
    }

    public void setClause (WhereClauseElement element) {
        if (!whereClauseElementList.contains(element)) {
            whereClauseElementList.add(element);
        }
    }

    public void removeClause (WhereClauseElement element) {
        whereClauseElementList.remove(element);
    }

}
