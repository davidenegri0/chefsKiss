package com.project.chefskiss;

import com.project.chefskiss.modelObjects.Piatto;
import com.project.chefskiss.modelObjects.Ristorante;
import com.project.chefskiss.modelObjects.Sede;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Piatto> PiattobyName = new Comparator<Piatto>() {
        @Override
        public int compare(Piatto o1, Piatto o2) {
            return o1.getNome().compareTo(o2.getNome());
        }
    };

    public static Comparator<Piatto> PiattobyVoto = new Comparator<Piatto>() {
        @Override
        public int compare(Piatto o1, Piatto o2) {
            return o1.getVotoMedio().compareTo(o2.getVotoMedio()) * -1;
        }
    };

    public static Comparator<Sede> SedeByVoto = new Comparator<Sede>() {
        @Override
        public int compare(Sede o1, Sede o2) { return o1.getVotoMedio().compareTo(o2.getVotoMedio()) * -1; }
    };

    public static Comparator<Ristorante> RistoranteByNome = new Comparator<Ristorante>() {
        @Override
        public int compare(Ristorante o1, Ristorante o2) { return o1.getNome().compareTo(o2.getNome()); }
    };
}
