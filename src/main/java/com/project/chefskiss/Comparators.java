package com.project.chefskiss;

import com.project.chefskiss.modelObjects.Piatto;

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
}
