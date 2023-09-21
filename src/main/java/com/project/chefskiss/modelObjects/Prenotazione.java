package com.project.chefskiss.modelObjects;

import java.sql.Time;
import java.sql.Date;

public class Prenotazione {
    private Date Data;
    private Time Orario;
    private Integer N_Posti;
    private User Utente;
    private Sede Sede;

    public Date getData() { return Data; }
    public void setData(Date data) { Data = data; }

    public Time getOrario() { return Orario; }
    public void setOrario(Time orario) { Orario = orario; }

    public Integer getN_Posti() { return N_Posti; }
    public void setN_Posti(Integer n_Posti) { N_Posti = n_Posti; }

    public User getUtenteP() { return Utente; }
    public void setUtenteP(User utente) { Utente = utente; }

    public Sede getSedeP() { return Sede; }
    public void setSedeP(Sede sede) { Sede = sede; }

}
