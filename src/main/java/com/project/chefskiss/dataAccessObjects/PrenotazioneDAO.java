package com.project.chefskiss.dataAccessObjects;

import com.project.chefskiss.modelObjects.Prenotazione;
import com.project.chefskiss.modelObjects.Sede;
import com.project.chefskiss.modelObjects.User;

import java.sql.Date;
import java.sql.Time;

public interface PrenotazioneDAO {
    public Prenotazione create (User utente, Sede sede, Date data, Time orario, Integer n_posti);
    public void update(Prenotazione prenotazione);
    public void delete(Prenotazione prenotazione);
}