package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;
import com.project.chefskiss.modelObjects.Valutazione;

import java.util.List;

public interface ValutazioneDAO {
    public Valutazione create(User utente, Sede sede, Integer voto);
    public void update(Valutazione valutazione);
    public void delete(Valutazione valutazione);
    public List<Valutazione> miglioriSedi (Integer numero);
    public boolean checkValutazione(String CF_recensore, String coordinate_sede);
    public Valutazione findByCF_Coordinate (String CF, String Coordinate);
    public List<Valutazione> findBySede (Sede sede);
}
